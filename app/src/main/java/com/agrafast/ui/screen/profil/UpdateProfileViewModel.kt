package com.agrafast.ui.screen.profil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.data.repository.UserRepository
import com.agrafast.domain.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
  private val userRepository: UserRepository
) : ViewModel() {
  var updateState: MutableStateFlow<UIState<Nothing>> = MutableStateFlow(UIState.Default)
    private set

  fun updateEmail(email: String, password: String) {
    viewModelScope.launch {
      updateState.emit(UIState.Loading)
      updateState.emitAll(userRepository.updateEmail(viewModelScope, email, password))
    }
  }

  fun updateProfil(name: String, phone: String){
    viewModelScope.launch {
      updateState.emit(UIState.Loading)
      updateState.emitAll(userRepository.updateProfile(name = name, phone = phone))
    }
  }



}