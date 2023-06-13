package com.agrafast.ui.screen.authetication.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.agrafast.R
import com.agrafast.ui.shape.BottomRoundedArcShape
import com.agrafast.ui.theme.AgraFastTheme

@Composable
fun AuthHeader() {
    val maxHeight = 180.dp
  Box(
    modifier = Modifier
      .sizeIn(maxHeight = 220.dp, minWidth = 360.dp)
      .fillMaxWidth()
      .clip(BottomRoundedArcShape()),
    contentAlignment = Alignment.BottomEnd
  ) {

    Image(
      painter = painterResource(id = R.drawable.auth_header),
      contentScale = ContentScale.Crop,
      contentDescription = null,
      modifier = Modifier.fillMaxSize()
    )
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          brush = Brush.verticalGradient(
            listOf(
              MaterialTheme.colorScheme.tertiary, Color.Transparent
            ),
            startY = Float.POSITIVE_INFINITY,
            endY = maxHeight.value - 64,
          )
        )
    )
      Text(
        text = stringResource(id = R.string.app_name).uppercase(),
        color = Color.White,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(end = 16.dp, bottom = 8.dp)
        )
  }
}

@Composable
fun AuthSubHeader(text1: String, text2: String) {
  Column(
    modifier = Modifier.padding(horizontal = 16.dp)
  ) {
    Text(
      modifier = Modifier.offset(y = 12.dp),
      text = text1,
      style = MaterialTheme.typography.displaySmall,
      color = MaterialTheme.colorScheme.tertiary
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      modifier = Modifier.alpha(0.7f),
      text = text2, style = MaterialTheme.typography.bodyMedium,
    )
  }
}

@Preview
@Composable
fun AuthHeaderPreview() {
  AgraFastTheme {
    AuthHeader()
  }
}

@Preview
@Composable
fun AuthSubHeaderPreview() {
  AgraFastTheme {
    AuthSubHeader("Hi, Welcome Back!", "Sign in to your AgraFast account.")
  }
}
