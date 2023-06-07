package com.agrafast.data.repository

import android.util.Log
import com.agrafast.data.firebase.model.User
import com.agrafast.domain.AuthState
import com.agrafast.domain.UIState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class UserRepository {
  private val auth = Firebase.auth
  val db = Firebase.firestore
  private val usersRef = db.collection("users")
  private val plantsRef = db.collection("plants")


  // First, login with email and password
  // Then, get user data from firestore
  suspend fun signInAndGetData(email: String, password: String): MutableStateFlow<AuthState<User>> {
    val result = MutableStateFlow<AuthState<User>>(AuthState.Loading)
    auth.signInWithEmailAndPassword(email, password)
      .addOnFailureListener {
        runBlocking { result.emit(AuthState.Error(it.message.toString())) }
        Log.d("TAG", "signInAndGetData: onFailure")
      }
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          Log.d("TAG", "signInAndGetData: taskSuccess")
          val authUser = auth.currentUser
          val uid = authUser?.uid!!
          usersRef.whereEqualTo("id", uid).get()
            .addOnFailureListener {
              runBlocking { result.emit(AuthState.Error(it.message.toString())) }
              Log.d("TAG", "signInAndGetData: onFailure")
            }.addOnCompleteListener {
              if (it.isSuccessful) {
                val res = it.result.documents.first()
                try {
                  val user = res.toObject<User>()?.setId(uid)?.setEmail(authUser.email!!)
                  result.tryEmit(AuthState.Authenticated(user))
                  Log.d("TAG", "signInAndGetData: authDataSuccess")
                } catch (e: Exception) {
                  result.tryEmit(AuthState.Error(e.message.toString()))
                  Log.d("TAG", "signInAndGetData: ${e.message.toString()}")
                }
              }
            }
        } else {
          Log.d("TAG", "signInAndGetData: taskFailed")
          result.tryEmit(AuthState.Unauthenticated)
        }
      }
    return result
  }

  suspend fun addToFav(userId: String, plantId: String): UIState<Nothing> {
    val plantMap = hashMapOf(
      "plantId" to plantId,
    )
    return try {
      usersRef.document(userId).collection("plants").add(plantMap).await()
      UIState.Success(null)
    } catch (e: Exception) {
      UIState.Error(e.message.toString())
    }
  }
}
