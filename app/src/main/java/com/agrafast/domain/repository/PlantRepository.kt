package com.agrafast.domain.repository

import android.util.Log
import com.agrafast.data.network.service.PlantApiService
import com.agrafast.domain.UIState
import com.agrafast.domain.model.Plant
import com.agrafast.domain.model.PlantDisease
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

  // TODO
//  suspend fun getPlantId(name: String): String?{
//    var id: String? = null
//    runBlocking {
//      try {
//        val res = plantRef.whereEqualTo("name", name).get().await().documents.first()
//        id = res.id
//      } catch (_: Exception){
//
//      }
//    }
//    return id
//  }


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
    if(diseaseName == null){
      emit(UIState.Error("Failed to get prediction"))
      return@flow
    }
    Log.d("TAG", "getPredictionDisease: $diseaseName")
    try {
      val res =
        plantRef.document(plant.id!!).collection("disease").whereEqualTo("name", diseaseName).get()
          .await().documents.first()
      Log.d("TAG", "getPredictionDisease: $res")
      val data = res.toObject(PlantDisease::class.java)
      emit(UIState.Success(data))
    } catch (e: Exception) {
      Log.d("TAG", "getPredictionDisease: ${e.message}")
      emit(UIState.Error(e.message!!))
    }
  }

  fun <T> CollectionReference.addSnapshotListenerFlow(dataType: Class<T>): Flow<UIState<List<T>>> =
    callbackFlow {
      val listener = object : EventListener<QuerySnapshot> {
        override fun onEvent(snapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
          if (error != null) {
            trySend(UIState.Error(error.localizedMessage!!.toString()))
            return
          }
          if (snapshot?.documents != null) {
            val list = snapshot.documents.mapNotNull { docSnapshot ->
              docSnapshot.toObject(dataType)
            }
            trySend(UIState.Success(list))
          }
        }
      }
      val registration = addSnapshotListener(listener)
      awaitClose { registration.remove() }
    }

  fun <T> DocumentReference.addSnapshotListenerFlow(dataType: Class<T>): Flow<UIState<T>> =
    callbackFlow {
      val listener = object : EventListener<DocumentSnapshot> {
        override fun onEvent(snapshot: DocumentSnapshot?, error: FirebaseFirestoreException?) {
          if (error != null) {
            trySend(UIState.Error(error.localizedMessage!!.toString()))
            return
          }
          if (snapshot != null && snapshot.exists()) {
            val data: T = snapshot.toObject(dataType)!!
            trySend(UIState.Success(data))
          }
        }
      }
      val registration = addSnapshotListener(listener)
      awaitClose { registration.remove() }
    }
}