package com.agrafast.domain

sealed class FetchStatus <out S> private constructor(){
  object Loading : FetchStatus<Nothing>()
  object Empty : FetchStatus<Nothing>()
  data class Success <out T>(val data: T? = null) : FetchStatus<T>()
  data class Error (val error: String) : FetchStatus<Nothing>()
}