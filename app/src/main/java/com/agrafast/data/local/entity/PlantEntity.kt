package com.agrafast.data.local.entity

import androidx.compose.ui.graphics.painter.Painter
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
class PlantEntity (
  @PrimaryKey
  val id: String,
  val name: String,
  val title: String,
  val imageUrl: String,
  )