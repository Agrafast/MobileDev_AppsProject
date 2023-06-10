package com.agrafast.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrafast.data.firebase.model.User
import com.agrafast.data.repository.UserRepository
import com.agrafast.domain.AuthState
import com.agrafast.ui.screen.authetication.component.AuthType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val userRepository: UserRepository
) : ViewModel() {


  var userState: MutableStateFlow<AuthState<User>> = MutableStateFlow(AuthState.Default)
    private set

  fun signUp(
    name: String,
    email: String,
    phone: String,
    password: String,
  ) {
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

  fun resetUserState() {
    userState.value = AuthState.Default
  }

  fun getAuthErrorMessage(authType: AuthType): String? {
    if (userState.value is AuthState.Default || userState.value is AuthState.Loading) {
      return null
    } else if (userState.value is AuthState.InvalidUser) { // Login
      return "Email belum terdaftar!"
    } else if (userState.value is AuthState.InvalidPassword) { // Login
      return "Kata sandi yang dimasukkan salah!"
    } else if (userState.value is AuthState.EmailExist) { // Register
      return "Email telah terdaftar!"
    } else if (userState.value is AuthState.EmailMalformed) { // Register
      return "Email yang dimasukkan salah!"
    } else {
      return if (authType == AuthType.Login) "Gagal melakukan registrasi!" else "Percobaan masuk gagal!"
    }
  }
}