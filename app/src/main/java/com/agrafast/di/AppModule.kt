package com.agrafast.di

import android.content.Context
import com.agrafast.App
import com.agrafast.data.network.ApiServiceProvider
import com.agrafast.data.network.service.ElevationApiService
import com.agrafast.data.network.service.PlantApiService
import com.agrafast.data.repository.PlantRepository
import com.agrafast.data.repository.UserRepository
import com.agrafast.util.BASE_URL
import com.agrafast.util.ELEVATION_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides
  @Singleton
  fun provideAppContext(@ApplicationContext app: Context): App {
    return app as App
  }

  @Provides
  @Singleton
  fun providePlantApiService(): PlantApiService {
    return ApiServiceProvider.getApiService(PlantApiService::class.java, BASE_URL)
  }

  @Provides
  @Singleton
  fun provideElevationApiService(): ElevationApiService {
    return ApiServiceProvider.getApiService(ElevationApiService::class.java, ELEVATION_URL)
  }

  @Provides
  @Singleton
  fun providePlantRepository(
    plantApiService: PlantApiService,
//    dataStore: DataStore
  ): PlantRepository = PlantRepository(plantApiService)

  @Provides
  @Singleton
  fun provideUserRepository(elevationApiService: ElevationApiService): UserRepository =
    UserRepository(elevationApiService)
}