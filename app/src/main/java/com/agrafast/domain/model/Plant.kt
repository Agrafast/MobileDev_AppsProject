package com.agrafast.domain.model

data class Plant(
  val id: String? = "0", // TODO -> Fit with API
  val name: String,
  val title: String,
  val titleLatin: String = "Wingardium Leviosa",
  val image: Int,
  val description: String = "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.",
)