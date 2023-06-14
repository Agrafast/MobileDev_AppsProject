package com.agrafast.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.data.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
  private val preferenceRepository: PreferenceRepository
) : ViewModel() {
  val showOnBoardingState = MutableStateFlow(false)

  fun getIsFirstOpenStatus() {
    viewModelScope.launch {
      val res = preferenceRepository.isFirstOpen()
      if (res != false) {
        showOnBoardingState.value = true
      }
    }
  }

  fun disableNextOnboarding() {
    viewModelScope.launch {
      preferenceRepository.setFirstOpenFalse()
    }
  }
}
