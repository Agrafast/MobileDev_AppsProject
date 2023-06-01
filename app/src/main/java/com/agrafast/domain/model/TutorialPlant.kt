package com.agrafast.domain.model

import androidx.compose.ui.graphics.painter.Painter

data class TutorialPlant(
  override val name: String,
  override val title: String,
  val image: Painter,
  val id: String? = "0", // TODO -> Fit with API
) : Plant