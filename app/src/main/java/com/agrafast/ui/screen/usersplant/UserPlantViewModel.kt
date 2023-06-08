package com.agrafast.ui.screen.usersplant

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.AppState
import com.agrafast.data.firebase.model.Plant
import com.agrafast.data.repository.PlantRepository
import com.agrafast.data.repository.UserRepository
import com.agrafast.domain.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel @Inject constructor(
  private val plantRepository: PlantRepository,
  private val userRepository: UserRepository
) : ViewModel() {

  var userPlantsState: MutableStateFlow<UIState<List<Plant>>> =
    MutableStateFlow(UIState.Loading)
    private set

  fun getUserPlants(userId: String) {
    viewModelScope.launch {
      val res = userRepository.getUserPlants(userId)
      userPlantsState.emit(res)
    }
  }

  fun deleteFromUserPlant(appState: AppState, plantId: String) {
    viewModelScope.launch {
      val job = launch {
        delay(3000)
        val res = userRepository.deleteFromUserPlant(appState.user.id, plantId)
        if (res is UIState.Success) {
          val newPlants =
            (userPlantsState.value as UIState.Success<List<Plant>>).data!!.filter {
              it.id != plantId
            }
          userPlantsState.emit(UIState.Success(newPlants))
        } else {
          appState.showSnackbar("Gagal menghapus", "Urungkan")
        }
      }
      appState.showSnackbar("Berhasil dihapus", "Urungkan",
        onResult = { snackbarResult ->
          if (snackbarResult != SnackbarResult.ActionPerformed) {
            job.cancel()
          }
        })
    }
  }
}
