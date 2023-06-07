package com.agrafast.domain

sealed class AuthState<out S> {
  object Loading : AuthState<Nothing>()

  //  Email already registered
  object EmailExist : AuthState<Nothing>()

  // No User Found with corresponding email
  object InvalidUser : AuthState<Nothing>()

  // Wrong password
  object InvalidPassword : AuthState<Nothing>()

  // When the login exist, but there is no document for the user in firestore
  object UserDataNotExist : AuthState<Nothing>()
  object Unauthenticated : AuthState<Nothing>()

  data class Error(val error: String) : AuthState<Nothing>()
  data class Authenticated<out T>(val data: T? = null) : AuthState<T>()
}