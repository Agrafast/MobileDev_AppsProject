package com.agrafast.data.repository

import android.util.Log
import com.agrafast.data.firebase.model.Plant
import com.agrafast.data.firebase.model.User
import com.agrafast.domain.AuthState
import com.agrafast.domain.UIState
import com.agrafast.util.addUserSnapshotListenerFlow
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await

class UserRepository {
  private val auth = Firebase.auth
  val db = Firebase.firestore
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
              val user = User(authUser.uid, name, authUser.email!!, phone = phone)
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
      val plants = plantsRef.whereIn(FieldPath.documentId(), plantIds).get().await().mapNotNull {
        it.toObject(Plant::class.java).setId(it.id)
      }
      UIState.Success(plants)
    } catch (e: Exception) {
      UIState.Error(e.message.toString())
    }
  }
}
