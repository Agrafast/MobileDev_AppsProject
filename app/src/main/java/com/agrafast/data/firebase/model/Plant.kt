package com.agrafast.data.firebase.model

data class Plant constructor(
  val name: String = "",
  val title: String = "",
  val botanical_name: String = "",
  val image_url: String = "",
//  val description: String = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.",
): FirebaseObject {
  override lateinit var id: String
  override fun setId(id: String): Plant{
    this.id = id
    return this
  }
}