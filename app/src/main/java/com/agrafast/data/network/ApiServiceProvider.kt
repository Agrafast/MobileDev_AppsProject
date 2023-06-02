package com.agrafast.data.network

import com.agrafast.util.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceProvider {
  fun <T> getApiService(service : Class<T>) : T {
    val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val httpClient = OkHttpClient.Builder()
      .addInterceptor(logging)
//      .addInterceptor{
//        val request = it.request().newBuilder().addHeader("Authorization","token $AUTH_TOKEN").build()
//        it.proceed(request)
//      }
      .build()
    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(httpClient)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
    return retrofit.create(service)
  }
}