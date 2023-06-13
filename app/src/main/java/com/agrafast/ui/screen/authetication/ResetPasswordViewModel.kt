package com.agrafast.ui.screen.authetication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.AppState
import com.agrafast.data.repository.UserRepository
import com.agrafast.domain.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
  private val userRepository: UserRepository
) : ViewModel() {
  var resetState: MutableStateFlow<AuthState<Nothing>> = MutableStateFlow(AuthState.Default)
    private set

  fun resetPassword(appState: AppState, email: String) {
    viewModelScope.launch {
      resetState.emit(AuthState.Loading)
      resetState.emitAll(userRepository.resetPassword(email))
    }
  }
}