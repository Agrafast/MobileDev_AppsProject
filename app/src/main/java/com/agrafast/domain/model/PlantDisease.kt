package com.agrafast.domain.model

data class PlantDisease(
  val name: String = "",
  val title: String = "",
  val title_id: String = "",
  val cause: String = "",
  val medicine: String = "",
  val treatment: String = ""
) {
}

data class PlantDiseaseList(
  val diseases: List<PlantDisease> = listOf()
)