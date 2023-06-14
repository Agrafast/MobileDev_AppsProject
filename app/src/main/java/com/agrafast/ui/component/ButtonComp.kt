package com.agrafast.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
  modifier: Modifier = Modifier,
  background: Color = MaterialTheme.colorScheme.primary,
  color: Color = Color.White,
  text: String,
  onClick: () -> Unit,
  isLoading: Boolean = false
) {
  Box(
    Modifier
      .fillMaxWidth()
      .animateContentSize(), contentAlignment = Alignment.Center
  ) {
    Button(
      onClick = onClick,
      modifier = modifier
        .fillMaxWidth()
        .height(48.dp),
      contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = background,
        contentColor = color
      )
    ) {
      if (!isLoading) {
        Text(text, style = MaterialTheme.typography.titleSmall)
      }
    }
    if (isLoading) {
      CircularProgressIndicator(
        color = Color.White
      )
    }
  }
}