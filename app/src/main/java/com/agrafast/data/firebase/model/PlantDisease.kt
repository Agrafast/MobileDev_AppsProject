package com.agrafast.data.firebase.model

data class PlantDisease(
  val name: String = "",
  val title: String = "",
  val title_id: String = "",
  val cause: String = "",
  val medicine: String = "",
  val treatment: String = "",
): FirebaseObject {
  override lateinit var id: String
  override fun setId(id: String): PlantDisease {
    this.id = id
    return this
  }
}
