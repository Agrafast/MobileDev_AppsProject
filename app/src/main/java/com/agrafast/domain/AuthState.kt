package com.agrafast.domain

sealed class AuthState<out S> {
  object Loading : AuthState<Nothing>()

  //  Email already registered
  object EmailExist : AuthState<Nothing>() // SignUp

  // Email Malformed
  object EmailMalformed : AuthState<Nothing>() // SignUp

  // No User Found with corresponding email
  object InvalidUser : AuthState<Nothing>() // SignIn

  // Wrong password
  object InvalidPassword : AuthState<Nothing>() // SignIn

  // When the login exist, but there is no document for the user in firestore
  object UserDataNotExist : AuthState<Nothing>() // SignIn
  object Unauthenticated : AuthState<Nothing>()
  object Default : AuthState<Nothing>()

  data class Error(val error: String) : AuthState<Nothing>()
  data class Authenticated<out T>(val data: T? = null) : AuthState<T>()
}