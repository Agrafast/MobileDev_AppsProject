package com.agrafast.data.repository

import android.util.Log
import com.agrafast.data.firebase.model.Plant
import com.agrafast.data.firebase.model.PlantDisease
import com.agrafast.data.firebase.model.TutorialStep
import com.agrafast.data.network.service.PlantApiService
import com.agrafast.domain.UIState
import com.agrafast.util.addSnapshotListenerFlow
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class PlantRepository @Inject constructor(
  private val plantService: PlantApiService,
) {
  private val db = Firebase.firestore
  private val plantRef = db.collection("plants")

  fun getTutorialPlants():Flow<UIState<List<Plant>>>  {
    return plantRef.addSnapshotListenerFlow(Plant::class.java)
  }

  fun getPlantTutorial(plantId: String): Flow<UIState<List<TutorialStep>>> {
    val diseaseRef = plantRef.document(plantId).collection("tutorial")
    return diseaseRef.addSnapshotListenerFlow(TutorialStep::class.java)
  }

  fun getPlantDiseases(plantId: String): Flow<UIState<List<PlantDisease>>> {
    val diseaseRef = plantRef.document(plantId).collection("disease")
    return diseaseRef.addSnapshotListenerFlow(PlantDisease::class.java)
  }

  private suspend fun getPrediction(path: String, image: File): String? {
    val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
    val fileMultipart = MultipartBody.Part.createFormData("file", image.name, requestImageFile)
    var predicted: String? = null
    predicted = try {
      val response = plantService.getPrediction(path, fileMultipart)
      response.prediction
    } catch (e: Exception) {
      Log.d("TAG", "getPrediction: ${e.message.toString()}")
      null
    }
    return predicted
  }

  suspend fun getPredictionDisease(plant: Plant, file: File): Flow<UIState<PlantDisease>> = flow {
    val diseaseName: String? = getPrediction(plant.name, file)
    if (diseaseName == null) {
      emit(UIState.Error("Failed to get prediction"))
      return@flow
    }
    try {
      val res =
        plantRef.document(plant.id).collection("disease").whereEqualTo("name", diseaseName).get()
          .await().documents.first()
      val data = res.toObject(PlantDisease::class.java)
      emit(UIState.Success(data))
    } catch (e: Exception) {
      emit(UIState.Error(e.message!!))
    }
  }

}