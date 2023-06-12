package com.agrafast.data.network.service

import com.agrafast.data.network.response.ElevationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ElevationApiService {
  @GET("json")
  suspend fun getUserElevation(
    @Query("locations") locations: String,
    @Query("key") key: String = "AIzaSyDV59nGf3x1IY5pZzGzrzaroW6x_30SQA4"
  ): ElevationResponse
}