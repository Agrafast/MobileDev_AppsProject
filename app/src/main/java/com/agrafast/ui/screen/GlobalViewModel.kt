package com.agrafast.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.agrafast.domain.model.TutorialPlant

class GlobalViewModel:ViewModel() {
  var tutorialPlant: TutorialPlant? by mutableStateOf(null)
    private set

  fun setCurrentTutorialPlant(plant: TutorialPlant){
    tutorialPlant = plant
  }
}