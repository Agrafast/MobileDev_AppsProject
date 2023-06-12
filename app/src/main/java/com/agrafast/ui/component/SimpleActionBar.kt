package com.agrafast.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.agrafast.ui.theme.AgraFastTheme

@Composable
fun SimpleActionBar(
  title: String,
  onBackClicked: () -> Unit,
  isBackgroundTransparent: Boolean = false,
  content: (@Composable () -> Unit)? = {}
) {
  var rowModifier = Modifier.height(64.dp)
  if (isBackgroundTransparent) {
    rowModifier = rowModifier.background(
      brush = Brush.verticalGradient(
        listOf(
          Color.Black, Color.Transparent
        ),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
      )
    )
  }
  Row(
    modifier = rowModifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(onClick = onBackClicked) {
      Icon(
        imageVector = Icons.Default.ArrowBack, contentDescription = "Back",
        tint = if (isBackgroundTransparent) Color.White else Color.Unspecified
      )
    }
    Box(modifier = Modifier
      .fillMaxSize(),
    contentAlignment = Alignment.CenterStart) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = if (isBackgroundTransparent) Color.White else Color.Unspecified
      )
    }
    content?.invoke()
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    SimpleActionBar("Tanaman", {}) {

    }
  }
}