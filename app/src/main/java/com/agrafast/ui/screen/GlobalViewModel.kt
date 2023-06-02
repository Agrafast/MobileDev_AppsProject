package com.agrafast.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.agrafast.R
import com.agrafast.domain.model.Plant

class GlobalViewModel : ViewModel() {
  var tutorialPlant: Plant? by mutableStateOf(null)
    private set

  fun setCurrentTutorialPlant(plant: Plant) {
    tutorialPlant = plant
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
  fun getDummyTutorialPlants(): List<Plant> {
    return listOf(
      Plant(name = "potato", title = "Kentang", image = R.drawable.potato_banner),
      Plant(name = "maize", title = "Jagung", image = R.drawable.maize_banner),
      Plant(name = "rice", title = "Padi", image = R.drawable.rice_banner),
      Plant(name = "potato", title = "Kentang", image = R.drawable.potato_banner),
      Plant(name = "maize", title = "Jagung", image = R.drawable.maize_banner),
      Plant(name = "rice", title = "Padi", image = R.drawable.rice_banner),
      Plant(name = "potato", title = "Kentang", image = R.drawable.potato_banner),
      Plant(name = "maize", title = "Jagung", image = R.drawable.maize_banner),
      Plant(name = "rice", title = "Padi", image = R.drawable.rice_banner),
    )
  }
}