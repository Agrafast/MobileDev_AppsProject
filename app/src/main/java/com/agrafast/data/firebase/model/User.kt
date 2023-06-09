package com.agrafast.data.firebase.model

data class User constructor(
  override var id: String = "",
  val name: String = "",
  var email: String = "",
  var phone: String = ""
) : FirebaseObject {
  override fun setId(id: String): User {
    this.id = id
    return this
  }

  fun setEmail(email: String): User {
    this.email = email
    return this
  }
}