package com.agrafast.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.data.firebase.model.User
import com.agrafast.data.repository.UserRepository
import com.agrafast.domain.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val userRepository: UserRepository
) : ViewModel() {
  var userState: MutableStateFlow<AuthState<User>> = MutableStateFlow(AuthState.Loading)
    private set

  fun signIn(email: String = "xmirz@gmail.com", password: String = "xmirzz") {
    viewModelScope.launch {
      userState.emitAll(userRepository.signInAndGetData(email, password))
    }
  }
}