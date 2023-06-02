package com.agrafast.domain.repository

import com.agrafast.data.network.service.PlantApiService
import com.agrafast.domain.model.Plant
import javax.inject.Inject

class PlantRepository @Inject constructor(
  plantService: PlantApiService,
) {
  // TODO
  fun getDiseasePlants(): List<Plant> {
    return listOf()
  }
}