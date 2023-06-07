package com.agrafast.domain

sealed class AuthState<out S> {
  object Loading : AuthState<Nothing>()
  object NoData : AuthState<Nothing>()
  object Unauthenticated : AuthState<Nothing>()
  data class Error(val error: String) : AuthState<Nothing>()
  data class Authenticated<out T>(val data: T? = null) : AuthState<T>()
}