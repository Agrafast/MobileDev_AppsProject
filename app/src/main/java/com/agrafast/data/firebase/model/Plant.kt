package com.agrafast.data.firebase.model

import androidx.annotation.Keep

@Keep
class Plant : FirebaseObject {
  override lateinit var id: String
  var name: String = ""
  var title: String = ""
  var botanical_name: String = ""
  var image_url: String = ""

  constructor(
    name: String = "",
    title: String = "",
    botanical_name: String = "",
    image_url: String = "",
  ) {
    this.name = name
    this.title = title
    this.botanical_name = botanical_name
    this.image_url = image_url
  }

  constructor()

  override fun setId(id: String): Plant {
    this.id = id
    return this
  }
}