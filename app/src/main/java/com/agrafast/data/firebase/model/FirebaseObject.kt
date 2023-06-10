package com.agrafast.data.firebase.model

import androidx.annotation.Keep

@Keep
interface FirebaseObject {
  var id: String
  fun setId(id: String): FirebaseObject
}