package com.agrafast.data.firebase.model

import androidx.annotation.Keep

@Keep
class User : FirebaseObject {
  override lateinit var id: String
  lateinit var email: String


  var name: String = ""
  var phone: String = ""

  constructor(
    name: String,
    phone: String
  ) {
    this.name = name
    this.phone = phone
  }

  constructor()

  override fun setId(id: String): User {
    this.id = id
    return this
  }

  fun setEmail(email: String): User {
    this.email = email
    return this
  }
}