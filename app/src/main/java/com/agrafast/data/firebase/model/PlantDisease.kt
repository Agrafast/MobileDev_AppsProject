package com.agrafast.data.firebase.model

import androidx.annotation.Keep

@Keep
class PlantDisease : FirebaseObject {
  override lateinit var id: String

  var name: String = ""
  var title: String = ""
  var title_id: String = ""
  var cause: String = ""
  var medicine: String = ""
  var treatment: String = ""

  constructor(
    name: String,
    title: String,
    title_id: String,
    cause: String,
    medicine: String,
    treatment: String
  ) {
    this.name = name
    this.title = title
    this.title_id = title_id
    this.cause = cause
    this.medicine = medicine
    this.treatment = treatment
  }

  constructor()

  override fun setId(id: String): PlantDisease {
    this.id = id
    return this
  }
}
