package com.agrafast.ui.screen.detection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.domain.UIState
import com.agrafast.domain.model.Plant
import com.agrafast.domain.model.PlantDisease
import com.agrafast.domain.repository.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import java.io.File
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

  var currentImage = MutableStateFlow<File?>(null)
    private set
//  fun getPlantId(){
//    viewModelScope.launch {
//      val id = plantRepository.getPlantId("maize")
//      Log.d("TAG", "idCoy $id")
//    }
//  }

  fun setCurrentImage(file: File) {
    currentImage.value = file
  }

  fun getPlantDiseases() {
    viewModelScope.launch {
      val res = plantRepository.getPlantDiseases(currentPlant.id!!)
      plantDiseaseListState.emitAll(res)
    }
  }

  fun getPredictionDisease() {
    viewModelScope.launch {
      predictedDiseaseState.emit(UIState.Loading)
      val res = plantRepository.getPredictionDisease(currentPlant, currentImage.value!!)
      predictedDiseaseState.emitAll(res)
    }
  }
}
