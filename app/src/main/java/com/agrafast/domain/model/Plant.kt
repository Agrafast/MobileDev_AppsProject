package com.agrafast.domain.model

import androidx.compose.ui.graphics.painter.Painter

data class Plant(
  val id: String? = "0", // TODO -> Fit with API
  val name: String,
  val title: String,
  val image: Int,
)