package com.agrafast.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.agrafast.R
import com.agrafast.domain.model.Plant
import kotlinx.coroutines.flow.MutableStateFlow

class GlobalViewModel : ViewModel() {
  var tutorialPlant: Plant? by mutableStateOf(null)
    private set
  var detectionPlant: Plant? by mutableStateOf(null)
    private set

  var myPlants = MutableStateFlow(getDummyMyPlants(5))

  fun setCurrentTutorialPlant(plant: Plant) {
    tutorialPlant = plant
  }
  fun setCurrentDetectionPlant(plant: Plant) {
    detectionPlant = plant
  }

  fun removeMyPlant(plant: Plant) {
    myPlants.value = myPlants.value.filter { it != plant }
    Log.d("TAG", "removeMyPlant: ${myPlants.value.size}")
  }

  // TODO From API
  fun getDummyDiseasePlants(): List<Plant> {
    return listOf(
      Plant(name = "potato", title = "Kentang", image = R.drawable.potato_banner),
      Plant(name = "maize", title = "Jagung", image = R.drawable.maize_banner),
      Plant(name = "rice", title = "Padi", image = R.drawable.rice_banner),
    )
  }

  // TODO From API
  fun getDummyTutorialPlants(count: Int): List<Plant> {
    val names = listOf("potato", "rice", "maize")
    val titles = listOf("Kentang", "Padi", "Jagung")
    val images = listOf(R.drawable.potato_banner, R.drawable.rice_banner, R.drawable.maize_banner)
    val plants = mutableListOf<Plant>()
    for (i in 1..count) {
      val plant = Plant(
        id = i.toString(),
        name = names.random(),
        title = titles.random(),
        image = images.random()
      )
      plants.add(plant)
    }
    return plants
  }

  fun getDummyMyPlants(count: Int): List<Plant> = getDummyTutorialPlants(count)
}