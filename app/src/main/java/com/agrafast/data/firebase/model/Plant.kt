package com.agrafast.data.firebase.model

import androidx.annotation.Keep
import com.agrafast.domain.model.ElevationLevel

@Keep
class Plant : FirebaseObject {
  override lateinit var id: String
  var name: String = ""
  var title: String = ""
  var elevation: Int = 0
  var botanical_name: String = ""
  var image_url: String = ""

  constructor(
    name: String = "",
    title: String = "",
    elevation: Int = 0,
    botanical_name: String = "",
    image_url: String = "",
  ) {
    this.name = name
    this.title = title
    this.elevation = elevation
    this.botanical_name = botanical_name
    this.image_url = image_url
  }

  constructor()

  override fun setId(id: String): Plant {
    this.id = id
    return this
  }

  fun getElevationType(): ElevationLevel {
    return if (elevation == 2) {
      ElevationLevel.HIGH
    } else if (elevation == 1) {
      ElevationLevel.LOW
    } else ElevationLevel.BOTH
  }
}