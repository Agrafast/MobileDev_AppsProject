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
import kotlinx.coroutines.CoroutineStart
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

  fun deleteFromUserPlant(appState: AppState, plant: Plant, index: Int) {
    // Act like if the plant deleted
    viewModelScope.launch {
      val newPlants =
        (userPlantsState.value as UIState.Success<List<Plant>>).data!!.filter {
          it.id != plant.id
        }
      userPlantsState.emit(UIState.Success(newPlants))
    }

    val returnBackJob = appState.coroutineScope.launch(start = CoroutineStart.LAZY) {
      val newPlants =
        (userPlantsState.value as UIState.Success<List<Plant>>).data!!.toMutableList()
      newPlants.add(index, plant)
      userPlantsState.emit(UIState.Success(newPlants))
    }

    val deleteJob = appState.coroutineScope.launch {
      delay(4000) // SnackBar.Short Duration
      val res = userRepository.deleteFromUserPlant(appState.user.id, plant.id)
      if (res !is UIState.Success) {
        appState.showSnackbar("Gagal menghapus")
        returnBackJob.start()
      }
    }
    appState.showSnackbar("Berhasil dihapus", "Urungkan",
      onResult = { snackbarResult ->
        if (snackbarResult == SnackbarResult.ActionPerformed) {
          deleteJob.cancel()
          returnBackJob.start()
        }
      })

  }
}
