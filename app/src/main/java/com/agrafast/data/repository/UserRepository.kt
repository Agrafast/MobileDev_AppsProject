package com.agrafast.data.repository

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.agrafast.data.firebase.model.Plant
import com.agrafast.data.firebase.model.User
import com.agrafast.data.network.service.ElevationApiService
import com.agrafast.domain.AuthState
import com.agrafast.domain.UIState
import com.agrafast.domain.model.ElevationLevel
import com.agrafast.domain.model.LatLong
import com.agrafast.util.WRONG_PASSWORD_ERROR
import com.agrafast.util.addUserSnapshotListenerFlow
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class UserRepository @Inject constructor(
  private val elevationApiService: ElevationApiService
) {
  private val auth = Firebase.auth
  private val db = Firebase.firestore
  private val usersRef = db.collection("users")
  private val plantsRef = db.collection("plants")

  fun signUpAndCreateData(
    name: String,
    email: String,
    phone: String,
    password: String,
  ): MutableStateFlow<AuthState<User>> {
    val result: MutableStateFlow<AuthState<User>> = MutableStateFlow(AuthState.Loading)
    auth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          val authUser = auth.currentUser!!
          val userMap = hashMapOf(
            "name" to name,
            "phone" to phone,
          )
          usersRef.document(authUser.uid).set(userMap).addOnCompleteListener {
            if (it.isSuccessful) {
              val user = User(name, phone = phone).setId(authUser.uid).setEmail(authUser.email!!)
              result.tryEmit(AuthState.Authenticated(user))
            } else {
              result.tryEmit(AuthState.UserDataNotExist)
            }
          }
        } else {
          when (task.exception) {
            is FirebaseAuthUserCollisionException -> {
              result.tryEmit(AuthState.EmailExist)
            }

            is FirebaseAuthInvalidCredentialsException -> {
              result.tryEmit(AuthState.EmailMalformed)
            }

            else -> {
              Log.d("TAG", "signInAndGetData: taskFailed")
              result.tryEmit(AuthState.Unauthenticated)
            }
          }
        }
      }
    return result
  }


  // First, login with email and password
  // Then, get user data from firestore
  fun signInAndGetData(
    email: String,
    password: String
  ): MutableStateFlow<AuthState<User>> {
    val result: MutableStateFlow<AuthState<User>> = MutableStateFlow(AuthState.Loading)
    auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          val authUser = auth.currentUser!!
          usersRef.document(authUser.uid).get().addOnCompleteListener {
            if (it.isSuccessful && it.result.exists()) {
              try {
                val user =
                  it.result.toObject<User>()?.setId(authUser.uid)?.setEmail(authUser.email!!)
                result.tryEmit(AuthState.Authenticated(user))
                Log.d("TAG", "signInAndGetData: authDataSuccess")
              } catch (e: Exception) {
                result.tryEmit(AuthState.Error(e.message.toString()))
                Log.d("TAG", "signInAndGetData: ${e.message.toString()}")
              }
            } else if (it.isSuccessful && !it.result.exists()) {
              result.tryEmit(AuthState.UserDataNotExist)
            } else {
              result.tryEmit(AuthState.Error(it.exception?.message.toString()))
              Log.d("TAG", "signInAndGetData: ${it.exception?.message.toString()}")
            }
          }
        } else {
          when (task.exception) {
            is FirebaseAuthInvalidUserException -> {
              result.tryEmit(AuthState.InvalidUser)
            }

            is FirebaseAuthInvalidCredentialsException -> {
              result.tryEmit(AuthState.InvalidPassword)
            }

            else -> {
              Log.d("TAG", "signInAndGetData: taskFailed")
              result.tryEmit(AuthState.Unauthenticated)
            }
          }
        }
      }
    return result
  }

  fun signOut() {
    Firebase.auth.signOut()
  }

  fun checkSession(): Boolean {
    return Firebase.auth.currentUser != null
  }

  fun resetPassword(email: String): MutableStateFlow<AuthState<Nothing>> {
    val result: MutableStateFlow<AuthState<Nothing>> = MutableStateFlow(AuthState.Loading)
    auth.sendPasswordResetEmail(email).addOnCompleteListener {
      if (it.isSuccessful) {
        result.tryEmit(AuthState.Success)
      } else {
        result.tryEmit(AuthState.Error("Failed"))
      }
    }
    return result
  }

  private suspend fun reAuthenticateUser(password: String): AuthState<Nothing> {
    val credential = EmailAuthProvider
      .getCredential(auth.currentUser!!.email!!, password)
    return try {
      auth.currentUser!!.reauthenticate(credential).await()
      AuthState.Authenticated(null)
    } catch (e: Exception) {
      Log.d("TAG", "reAuthenticateUser: ${e.message}")
      AuthState.Unauthenticated
    }
  }

  suspend fun updateEmail(
    scope: CoroutineScope,
    email: String,
    password: String
  ): MutableStateFlow<UIState<Nothing>> {
    val result: MutableStateFlow<UIState<Nothing>> = MutableStateFlow(UIState.Loading)
    val reAuth = scope.async { return@async reAuthenticateUser(password) }.await()
    if (reAuth is AuthState.Unauthenticated) {
      result.tryEmit(UIState.Error(WRONG_PASSWORD_ERROR))
    } else {
      auth.currentUser!!.updateEmail(email).addOnCompleteListener {
        if (it.isSuccessful) {
          result.tryEmit(UIState.Success(null))
        } else {
          result.tryEmit(UIState.Error("Failed"))
        }
      }
    }
    return result
  }

  suspend fun updatePassword(
    scope: CoroutineScope,
    oldPassword: String,
    newPassword: String,
  ): MutableStateFlow<UIState<Nothing>> {
    val result: MutableStateFlow<UIState<Nothing>> = MutableStateFlow(UIState.Loading)
    val reAuth = scope.async { return@async reAuthenticateUser(oldPassword) }.await()
    if (reAuth is AuthState.Unauthenticated) {
      result.tryEmit(UIState.Error(WRONG_PASSWORD_ERROR))
    } else {
      auth.currentUser!!.updatePassword(newPassword).addOnCompleteListener { task ->
        if (task.isSuccessful) {
          result.tryEmit(UIState.Success(null))
        } else {
          result.tryEmit(UIState.Error("Failed"))
        }
      }
    }
    Log.d("TAG", "updatePassword: $reAuth")
    return result
  }

  fun updateProfile(name: String, phone: String): MutableStateFlow<UIState<Nothing>> {
    val authUser = auth.currentUser!!
    val result: MutableStateFlow<UIState<Nothing>> = MutableStateFlow(UIState.Loading)
    val userMap = hashMapOf(
      "name" to name,
      "phone" to phone,
    )
    usersRef.document(authUser.uid).set(userMap).addOnCompleteListener {
      if (it.isSuccessful) {
        result.tryEmit(UIState.Success())
      } else {
        result.tryEmit(UIState.Error("Failed"))
      }
    }
    return result
  }

  fun getUserData(): Flow<AuthState<User>> {
    val authUser = auth.currentUser
    val userId = authUser?.uid!!
    return usersRef.document(userId).addUserSnapshotListenerFlow(userId, authUser.email!!)
  }


  suspend fun addToUserPlant(userId: String, plantId: String): UIState<Nothing> {
    val plantMap = hashMapOf(
      "plantId" to plantId,
    )
    return try {
      usersRef.document(userId).collection("plants").document(plantId).set(plantMap).await()
      UIState.Success(null)
    } catch (e: Exception) {
      UIState.Error(e.message.toString())
    }
  }

  suspend fun deleteFromUserPlant(userId: String, plantId: String): UIState<Nothing> {
    return try {
      usersRef.document(userId).collection("plants").document(plantId).delete().await()
      UIState.Success(null)
    } catch (e: Exception) {
      UIState.Error(e.message.toString())
    }
  }

  suspend fun checkIfPlantInUserPlant(userId: String, plantId: String): UIState<Boolean> {
    return try {
      val res = usersRef.document(userId).collection("plants").document(plantId).get().await()
      UIState.Success(res.exists())
    } catch (e: Exception) {
      UIState.Error(e.message.toString())
    }
  }

  suspend fun getUserPlants(userId: String): UIState<List<Plant>> {
    return try {
      val plantIds = usersRef.document(userId).collection("plants").get().await().documents.map {
        it.id
      }
      if (plantIds.isEmpty()) return UIState.Empty
      val plants = plantsRef.whereIn(FieldPath.documentId(), plantIds).get().await().mapNotNull {
        it.toObject(Plant::class.java).setId(it.id)
      }
      UIState.Success(plants)
    } catch (e: Exception) {
      UIState.Error(e.message.toString())
    }
  }

  fun getReadableLocation(latitude: Double, longitude: Double, context: Context): String? {
    var addressText = ""
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
      val addresses = geocoder.getFromLocation(latitude, longitude, 1)
      if (addresses?.isNotEmpty() == true) {
        val address = addresses.first()
        addressText = "${address.locality}, ${address.subAdminArea}, ${address.adminArea}"
        Log.d("geolocation", addressText)
      }
      addressText
    } catch (e: IOException) {
      Log.d("geolocation", e.message.toString())
      null
    }
  }

  suspend fun getUserElevation(latLong: LatLong): ElevationLevel {
    val locations = "${latLong.latitude},${latLong.longitude}"
    var elevation: Double? = null
    elevation = try {
      val response = elevationApiService.getUserElevation(locations)
      Log.d("TAG", "getUserElevation: ${response.results.first().elevation} ")
      response.results.first().elevation
    } catch (e: Exception) {
      Log.d("TAG", "getUserElevation: ${e.message.toString()} ")
      null
    }
    val elevationLevel = if (elevation == null) {
      ElevationLevel.BOTH
    } else if (elevation <= 200.0) {
      ElevationLevel.LOW
    } else {
      ElevationLevel.HIGH
    }
    return elevationLevel
  }
}
