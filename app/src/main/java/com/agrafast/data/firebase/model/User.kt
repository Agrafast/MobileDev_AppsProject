package com.agrafast.data.firebase.model

data class User constructor(
  val name: String = "",
//  val description: String = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.",
) : FirebaseObject {
  override lateinit var id: String
  lateinit var email: String
  override fun setId(id: String): User {
    this.id = id
    return this
  }

  fun setEmail(email: String): User {
    this.email = email
    return this
  }
}