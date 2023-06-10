package com.agrafast.data.firebase.model

import androidx.annotation.Keep

@Keep
class TutorialStep : FirebaseObject {
  override lateinit var id: String

  var tahap_id: Long = 0
  var tahapan_menanam: String = ""
  var detail_kegiatan: String = ""

  constructor(
    tahap_id: Long,
    tahapan_menanam: String,
    detail_kegiatan: String
  ) {
    this.tahap_id = tahap_id
    this.tahapan_menanam = tahapan_menanam
    this.detail_kegiatan = detail_kegiatan
  }

  constructor()

  override fun setId(id: String): TutorialStep {
    this.id = id
    return this
  }
}