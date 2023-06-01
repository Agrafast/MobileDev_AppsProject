package com.agrafast.domain.model

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.painter.Painter

data class DiseasePlant(
  override val name: String,
  override val title: String,
  val image: Painter
) : Plant