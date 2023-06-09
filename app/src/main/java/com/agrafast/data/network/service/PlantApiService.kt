package com.agrafast.data.network.service

import com.agrafast.data.network.response.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PlantApiService {
  @Multipart
  @POST("index/{plant}")
  suspend fun getPrediction(
    @Path("plant") plant: String,
    @Part file: MultipartBody.Part
  ): PredictionResponse
}