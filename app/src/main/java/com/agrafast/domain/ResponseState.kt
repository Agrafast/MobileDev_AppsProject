package com.agrafast.domain

sealed class ResponseState<out S> {
  object Loading : ResponseState<Nothing>()
  object Empty : ResponseState<Nothing>()
  object Retrying : ResponseState<Nothing>()
  data class Success<out T>(val data: T? = null) : ResponseState<T>()
  data class Error(val error: String) : ResponseState<Nothing>()
}