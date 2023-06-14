package com.agrafast.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.agrafast.App
import com.agrafast.data.network.ApiServiceProvider
import com.agrafast.data.network.service.ElevationApiService
import com.agrafast.data.network.service.PlantApiService
import com.agrafast.data.repository.PlantRepository
import com.agrafast.data.repository.PreferenceRepository
import com.agrafast.data.repository.UserRepository
import com.agrafast.util.BASE_URL
import com.agrafast.util.ELEVATION_URL
import com.agrafast.util.USER_PREFERENCE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

  @Provides
  @Singleton
  fun providePreferenceRepository(dataStore: DataStore<Preferences>): PreferenceRepository =
    PreferenceRepository(dataStore)

  @Provides
  @Singleton
  fun providePreferencesDataStore(
    @ApplicationContext app: Context
  ): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
      corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
      migrations = listOf(SharedPreferencesMigration(app, USER_PREFERENCE)),
      scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
      produceFile = { app.preferencesDataStoreFile(USER_PREFERENCE) }
    )
  }
}