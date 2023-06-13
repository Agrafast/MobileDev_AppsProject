package com.agrafast.ui.screen.profil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.data.repository.UserRepository
import com.agrafast.domain.UIState
import com.agrafast.util.WRONG_PASSWORD_ERROR
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

  var errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
  fun updateEmail(email: String, password: String) {
    viewModelScope.launch {
      updateState.emit(UIState.Loading)
      updateState.emitAll(userRepository.updateEmail(viewModelScope, email, password))
    }
  }

  fun updatePassword(oldPassword: String, newPassword: String) {
    viewModelScope.launch {
      updateState.emit(UIState.Loading)
      updateState.emitAll(
        userRepository.updatePassword(
          viewModelScope,
          oldPassword = oldPassword,
          newPassword = newPassword
        )
      )
    }
  }

  fun updateProfil(name: String, phone: String) {
    viewModelScope.launch {
      updateState.emit(UIState.Loading)
      updateState.emitAll(userRepository.updateProfile(name = name, phone = phone))
    }
  }

  fun getErrorMessage() {
    if (updateState.value is UIState.Error && (updateState.value as UIState.Error).error == WRONG_PASSWORD_ERROR) {
      errorMessage.value = "Kata sandi lama salah!"
    } else if (updateState.value is UIState.Error) {
      errorMessage.value = "Gagal memperbarui kata sandi"
    } else {
      errorMessage.value = null
    }
  }

}