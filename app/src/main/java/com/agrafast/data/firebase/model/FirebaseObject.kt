package com.agrafast.data.firebase.model

interface FirebaseObject {
  val id: String
  fun addId(id: String): FirebaseObject
}