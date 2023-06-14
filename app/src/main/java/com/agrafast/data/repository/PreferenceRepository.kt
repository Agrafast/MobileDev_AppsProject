package com.agrafast.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private object PreferenceKeys {
  val IS_FIRST_INSTALL = booleanPreferencesKey("isFirstInstall")
}

class PreferenceRepository @Inject constructor(
  private val dataStore: DataStore<Preferences>,
) {
  suspend fun isFirstOpen(): Boolean? {
    return dataStore.data.map {
      it[PreferenceKeys.IS_FIRST_INSTALL]
    }.firstOrNull()
  }

  suspend fun setFirstOpenFalse() {
    dataStore.edit {
      it[PreferenceKeys.IS_FIRST_INSTALL] = false
    }
  }
}