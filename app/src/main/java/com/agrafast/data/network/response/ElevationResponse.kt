package com.agrafast.data.network.response

data class ElevationResponse(
  var results: List<ElevationResult>,
  val status: String
)

data class ElevationResult(
  val elevation: Double,
  val location: Map<String, Double>,
  val resolution: Double
)