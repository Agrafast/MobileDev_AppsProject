package com.agrafast.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.AppState
import com.agrafast.data.firebase.model.TutorialStep
import com.agrafast.data.repository.PlantRepository
import com.agrafast.data.repository.UserRepository
import com.agrafast.domain.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPlantViewModel @Inject constructor(
  private val plantRepository: PlantRepository,
  private val userRepository: UserRepository
) : ViewModel() {


  var tutorialsState: MutableStateFlow<UIState<List<TutorialStep>>> =
    MutableStateFlow(UIState.Loading)
    private set

  var isInUserPlantState: MutableStateFlow<Boolean> =
    MutableStateFlow(false)
    private set

  fun getPlantTutorial(plantId: String) {
    viewModelScope.launch {
      val res = plantRepository.getPlantTutorial(plantId)
      tutorialsState.emitAll(res)
    }
  }

  fun insertToUserPlant(appState: AppState, plantId: String) {
    viewModelScope.launch {
      val res = userRepository.addToUserPlant(appState.user.id, plantId)
      if(res is UIState.Success){
        isInUserPlantState.emit(true)
        appState.showSnackbar("Berhasil menambahkan ke tanamanku")
      } else {
        isInUserPlantState.emit(false)
        appState.showSnackbar("Gagal menambahkan ke tanamanku")
      }
    }
  }

  fun deleteFromUserPlant(appState: AppState, plantId: String) {
    viewModelScope.launch {
      val res = userRepository.deleteFromUserPlant(appState.user.id, plantId)
      if(res is UIState.Success){
        isInUserPlantState.emit(false)
        appState.showSnackbar("Berhasil menghapus dari tanamanku")
      } else {
        appState.showSnackbar("Gagal menghapus dari tanamanku")
      }
    }
  }

  fun checkIsInUserPlant(userId: String, plantId: String) {
    viewModelScope.launch {
      val res = userRepository.checkIfPlantInUserPlant(userId, plantId)
      if(res is UIState.Success){
        isInUserPlantState.emit(res.data!!)
      }
    }
  }


}
