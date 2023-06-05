package com.agrafast.domain

sealed class UIState<out S> {
  object Loading : UIState<Nothing>()
  object Default : UIState<Nothing>()
  object Empty : UIState<Nothing>()
  object Retrying : UIState<Nothing>()
  data class Success<out T>(val data: T? = null) : UIState<T>()
  data class Error(val error: String) : UIState<Nothing>()
}