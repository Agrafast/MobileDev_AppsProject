package com.agrafast.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.data.firebase.model.User
import com.agrafast.data.repository.UserRepository
import com.agrafast.domain.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val userRepository: UserRepository
) : ViewModel() {


  var userState: MutableStateFlow<AuthState<User>> = MutableStateFlow(AuthState.Unauthenticated)
    private set

  fun signUp(
    name: String,
    email: String,
    phone: String,
    password: String,
  ){
    viewModelScope.launch {
      userState.emitAll(userRepository.signUpAndCreateData(name, email, phone, password))
    }
  }
  fun signIn(
    email: String,
    password: String
  ) {
    viewModelScope.launch {
      userState.emitAll(userRepository.signInAndGetData(email, password))
    }
  }

  fun signOut() {
    viewModelScope.launch {
      userRepository.signOut()
      userState.emit(AuthState.Unauthenticated)
    }
  }

  fun checkSession() {
    val sessionExist = userRepository.checkSession()
    viewModelScope.launch {
      if (sessionExist) {
        val res = userRepository.getUserData()
        userState.emitAll(res)
      } else {
        userState.emit(AuthState.Unauthenticated)
      }
    }
  }
}