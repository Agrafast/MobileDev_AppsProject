package com.agrafast.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agrafast.util.OurColor

@Composable
fun HomeScreen() {
  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    Row(
      modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
      Box(
        modifier = Modifier
          .background(color = OurColor.gray100, CircleShape)
          .size(64.dp)
      ) {
        Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.fillMaxSize()
          .padding(8.dp))
      }
      Spacer(modifier = Modifier.width(16.dp))
      Column() {
        Text(
          text = "Indi nih Boss", fontWeight = FontWeight.SemiBold, fontSize = 20.sp, modifier =
          Modifier.fillMaxWidth()
        )
        Text(
          text = "Senggol dongg!!!!!", fontWeight = FontWeight.Normal, fontSize = 16.sp,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}