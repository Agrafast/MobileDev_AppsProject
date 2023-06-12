package com.agrafast.domain.model

class LatLong(
  val latitude: Double = 0.0,
  val longitude: Double = 0.0
)

enum class ElevationLevel(val level: Int) {
  HIGH(2),
  LOW(1),
  BOTH(0)
}