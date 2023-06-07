package com.agrafast.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.data.firebase.model.Plant
import com.agrafast.domain.UIState
import com.agrafast.data.repository.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel @Inject constructor(
  private val plantRepository: PlantRepository
) : ViewModel() {
  var tutorialPlant: Plant? by mutableStateOf(null)
    private set
  var detectionPlant: Plant? by mutableStateOf(null)
    private set

  var tutorialPlantsState: MutableStateFlow<UIState<List<Plant>>> =
    MutableStateFlow(UIState.Loading)
    private set

  var myPlants = MutableStateFlow<List<Plant>>(listOf())

  fun setCurrentTutorialPlant(plant: Plant?) {
    tutorialPlant = plant
  }
  fun setCurrentDetectionPlant(plant: Plant?) {
    detectionPlant = plant
  }

  fun removeMyPlant(plant: Plant) {
    myPlants.value = myPlants.value.filter { it != plant }
    Log.d("TAG", "removeMyPlant: ${myPlants.value.size}")
  }

  // TODO From API
  fun fetchPredictionPlants(): List<Plant> {
    return listOf(
      Plant(name = "potato", title = "Kentang", botanical_name = "Solanum Tuberosum" , image_url = "https://firebasestorage.googleapis.com/v0/b/plant-tutorials/o/banner%2Fpotato.webp?alt=media&token=24ee06a3-c3ae-463f-8334-36f1e079596f&_gl=1*6k4xco*_ga*MzUwOTU2NjU3LjE2ODMyMTE3Mjc.*_ga_CW55HF8NVT*MTY4NTk3ODkyMC4xOS4xLjE2ODU5ODA1OTAuMC4wLjA.").setId("SN08zvXW5DmLHuxlC3mJ"),
      Plant(name = "maize", title = "Jagung", botanical_name = "Zea mays", image_url = "https://firebasestorage.googleapis.com/v0/b/plant-tutorials/o/banner%2Fmaize.jpg?alt=media&token=b23d821e-b873-4e96-89b7-c9c5dcfeb71a&_gl=1*5ief8k*_ga*MzUwOTU2NjU3LjE2ODMyMTE3Mjc.*_ga_CW55HF8NVT*MTY4NTk3ODkyMC4xOS4xLjE2ODU5ODA2MjcuMC4wLjA.").setId("XhafVgIsbocxmrelOJK8"),
      Plant(name = "rice", title = "Padi", botanical_name = "Oryza sativa", image_url = "https://firebasestorage.googleapis.com/v0/b/plant-tutorials/o/banner%2Frice.webp?alt=media&token=80f2c70a-ced8-4989-b652-6b2f29c1be14&_gl=1*zsova*_ga*MzUwOTU2NjU3LjE2ODMyMTE3Mjc.*_ga_CW55HF8NVT*MTY4NTk3ODkyMC4xOS4xLjE2ODU5ODA2ODIuMC4wLjA.").setId("dasr9Kae7uQhYc1GWwhC"),
    )
  }

  fun fetchTutorialPlants() {
    viewModelScope.launch {
      val res = plantRepository.getTutorialPlants()
      tutorialPlantsState.emitAll(res)
    }
  }

  fun getTutorialPlants(): List<Plant>{
    return (tutorialPlantsState.value as UIState.Success<List<Plant>>).data!!
  }

  fun getShowDetection(name: String): Boolean{
    return listOf("maize", "rice", "potato").contains(name)
  }
}