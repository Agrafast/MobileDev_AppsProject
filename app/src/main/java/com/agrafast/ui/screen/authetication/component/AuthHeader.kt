package com.agrafast.ui.screen.authetication.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.agrafast.R

@Composable
fun AuthHeader(){
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .shadow(6.dp),
    contentAlignment = Alignment.CenterStart
  ) {
    Image(
      painter = painterResource(id = R.drawable.authetication_banner),
      contentScale = ContentScale.Crop,
      contentDescription = null,
      modifier = Modifier
        .sizeIn(maxHeight = 180.dp, minWidth = 360.dp)
        .fillMaxWidth()
    )
    Column {
      Text(
        text = stringResource(id = R.string.welcome_text).uppercase(),
        color = Color.White,
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier.padding(start = 16.dp),

        )
    }
  }

}


@Composable
fun AuthSubHeader(){
  Column {
    Text(
      text = "Login to start with", style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(top = 16.dp, start = 16.dp)
    )
    Text(
      text = "Ultimate Farming App", style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(start = 16.dp)
    )
  }
}