package com.agrafast.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.agrafast.data.firebase.model.FirebaseObject
import com.agrafast.data.firebase.model.User
import com.agrafast.domain.AuthState
import com.agrafast.domain.UIState
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun <T : FirebaseObject> CollectionReference.addSnapshotListenerFlow(dataType: Class<T>): Flow<UIState<List<T>>> =
  callbackFlow {
    val listener = object : EventListener<QuerySnapshot> {
      override fun onEvent(snapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
          trySend(UIState.Error(error.localizedMessage!!.toString()))
          return
        }
        if (snapshot?.documents != null) {
          val list: List<T> = snapshot.documents.mapNotNull { docSnapshot ->
            docSnapshot.toObject(dataType)!!.setId(docSnapshot.id) as T
          }
          trySend(UIState.Success(list))
        }
      }
    }
    val registration = addSnapshotListener(listener)
    awaitClose { registration.remove() }
  }

fun <T> DocumentReference.addSnapshotListenerFlow(dataType: Class<T>): Flow<UIState<T>> =
  callbackFlow {
    val listener = object : EventListener<DocumentSnapshot> {
      override fun onEvent(snapshot: DocumentSnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
          trySend(UIState.Error(error.localizedMessage!!.toString()))
          return
        }
        if (snapshot != null && snapshot.exists()) {
          val data: T = snapshot.toObject(dataType)!!
          trySend(UIState.Success(data))
        }
      }
    }
    val registration = addSnapshotListener(listener)
    awaitClose { registration.remove() }
  }

fun DocumentReference.addUserSnapshotListenerFlow(
  userId: String,
  email: String
): Flow<AuthState<User>> =
  callbackFlow {
    val listener = object : EventListener<DocumentSnapshot> {
      override fun onEvent(snapshot: DocumentSnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
          trySend(AuthState.Error(error.message.toString()))
          return
        }
        if (snapshot != null && snapshot.exists()) {
          val data = snapshot.toObject(User::class.java)?.setId(userId)?.setEmail(email)
          trySend(AuthState.Authenticated(data))
        }
        if (snapshot != null && !snapshot.exists()) {
          trySend(AuthState.UserDataNotExist)
        }
      }
    }
    val registration = addSnapshotListener(listener)
    awaitClose { registration.remove() }
  }


val timeStamp: String = SimpleDateFormat("dd-MMM-yyy", Locale("id")).format(Date())

fun Context.createTempFile(): File {
  return File.createTempFile(timeStamp, ".jpg", externalCacheDir)
}


fun Context.createFileFromUri(selectedImg: Uri): File {
  val contentResolver: ContentResolver = this.contentResolver
  val myFile = this.createTempFile()

  val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
  val outputStream: OutputStream = FileOutputStream(myFile)
  val buf = ByteArray(1024)
  var len: Int
  while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
  outputStream.close()
  inputStream.close()
  return myFile
}

suspend fun File.reduceFileImage(max: Int = 1000000): File {
  val bitmap = BitmapFactory.decodeFile(this.path)
  var compressQuality = 100
  var streamLength: Int
  do {
    val bmpStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
    val bmpPicByteArray = bmpStream.toByteArray()
    streamLength = bmpPicByteArray.size
    compressQuality -= 5
  } while (streamLength > max && compressQuality > 50)
  bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(this))
  return this
}