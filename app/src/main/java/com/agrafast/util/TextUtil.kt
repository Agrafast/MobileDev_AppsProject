package com.agrafast.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import com.agrafast.domain.model.Plant

class TextUtil {
  companion object{
    fun buildPlantNameWithLatin(plant: Plant) : AnnotatedString{
      val title = buildAnnotatedString {
        append(plant.title)
        append(" (")
        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
          append(plant.titleLatin)
        }
        append(")")
      }
      return title
    }
  }
}