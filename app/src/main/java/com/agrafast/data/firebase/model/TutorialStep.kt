package com.agrafast.data.firebase.model

data class TutorialStep constructor(
  val tahap_id: Int = 0,
  val tahapan_menanam: String = "",
  val detail_kegiatan: String = "",
//  val description: String = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.",
): FirebaseObject {
  override lateinit var id: String
  override fun addId(id: String): TutorialStep{
    this.id = id
    return this
  }
}