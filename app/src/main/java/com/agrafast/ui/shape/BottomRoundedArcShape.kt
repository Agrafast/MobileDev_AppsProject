package com.agrafast.ui.shape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class BottomRoundedArcShape:Shape {
  override fun createOutline(
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density
  ): Outline {
    return Outline.Generic(
      path = drawArcPath(size = size)
    )
  }

  private fun drawArcPath(size: Size): Path {
    return Path().apply {
      reset()
      lineTo(size.width, 0f)
      lineTo(size.width, size.height)

      arcTo(
        rect = Rect(Offset(0f, size.height/2), Size(width = size.width*2, height = size.height/2)),
        startAngleDegrees = 90f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
      )

      lineTo(0f, 0f)
      close()
    }
  }
}