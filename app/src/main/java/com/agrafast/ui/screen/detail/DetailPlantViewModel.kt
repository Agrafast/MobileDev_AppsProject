package com.agrafast.ui.screen.detail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.domain.UIState
import com.agrafast.data.firebase.model.Plant
import com.agrafast.data.firebase.model.PlantDisease
import com.agrafast.data.firebase.model.TutorialStep
import com.agrafast.domain.repository.PlantRepository
import com.agrafast.util.createFileFromUri
import com.agrafast.util.reduceFileImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPlantViewModel @Inject constructor(
  private val plantRepository: PlantRepository
) : ViewModel() {


  var tutorialsState: MutableStateFlow<UIState<List<TutorialStep>>> =
    MutableStateFlow(UIState.Loading)
    private set

  fun getPlantTutorial(plantId: String){
    viewModelScope.launch {
      val res = plantRepository.getPlantTutorial(plantId)
      tutorialsState.emitAll(res)
    }
  }
}
