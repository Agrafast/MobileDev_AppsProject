package com.agrafast.data.repository

import android.util.Log
import com.agrafast.data.firebase.model.User
import com.agrafast.domain.AuthState
import com.agrafast.domain.UIState
import com.agrafast.util.addUserSnapshotListenerFlow
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
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


  // First, login with email and password
  // Then, get user data from firestore
  fun signInAndGetData(
    email: String,
    password: String
  ): MutableStateFlow<AuthState<User>> {
    val result: MutableStateFlow<AuthState<User>> = MutableStateFlow(AuthState.Loading)
    auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener { task ->
        val authUser = auth.currentUser
        val userId = authUser?.uid!!
        Log.d("TAG", "signInAndGetData: taskStart")
        if (task.isSuccessful) {
          Log.d("TAG", "signInAndGetData: taskSuccess")
          usersRef.document(userId).get().addOnCompleteListener {
            if (it.isSuccessful && it.result.exists()) {
              try {
                val user = it.result.toObject<User>()?.setId(userId)?.setEmail(authUser.email!!)
                result.tryEmit(AuthState.Authenticated(user))
                Log.d("TAG", "signInAndGetData: authDataSuccess")
              } catch (e: Exception) {
                result.tryEmit(AuthState.Error(e.message.toString()))
                Log.d("TAG", "signInAndGetData: ${e.message.toString()}")
              }
            } else if (it.isSuccessful && it.result.exists()) {
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
      val task = usersRef.document(userId).collection("plants").document(plantId).delete().await()
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
}
