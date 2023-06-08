package com.agrafast.data.firebase.model

interface FirebaseObject {
  val id: String
  fun setId(id: String): FirebaseObject
}