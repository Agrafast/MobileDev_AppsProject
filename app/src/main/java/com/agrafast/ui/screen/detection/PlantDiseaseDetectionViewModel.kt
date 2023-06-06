package com.agrafast.ui.screen.detection

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.domain.UIState
import com.agrafast.data.firebase.model.Plant
import com.agrafast.data.firebase.model.PlantDisease
import com.agrafast.domain.repository.PlantRepository
import com.agrafast.util.createFileFromUri
import com.agrafast.util.reduceFileImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantDiseaseDetectionViewModel @Inject constructor(
  private val plantRepository: PlantRepository
) : ViewModel() {

  lateinit var currentPlant: Plant

  var plantDiseaseListState: MutableStateFlow<UIState<List<PlantDisease>>> =
    MutableStateFlow(UIState.Loading)
    private set

  var predictedDiseaseState: MutableStateFlow<UIState<PlantDisease>> =
    MutableStateFlow(UIState.Default)
    private set

  var currentImage = MutableStateFlow<Uri?>(null)
    private set

//  fun getPlantId(){
//    viewModelScope.launch {
//      val id = plantRepository.getPlantId("maize")
//      Log.d("TAG", "idCoy $id")
//    }
//  }

  fun setCurrentImage(uri: Uri) {
    currentImage.value = uri
  }

  fun getPlantDiseases() {
    viewModelScope.launch {
      val res = plantRepository.getPlantDiseases(currentPlant.id!!)
      plantDiseaseListState.emitAll(res)
    }
  }

  fun getPredictionDisease(context : Context) {
    viewModelScope.launch {
      predictedDiseaseState.emit(UIState.Loading)
      val imageFile = context.createFileFromUri(currentImage.value!!).reduceFileImage(150000)
      val res = plantRepository.getPredictionDisease(currentPlant, imageFile)
      predictedDiseaseState.emitAll(res)
    }
  }
}
