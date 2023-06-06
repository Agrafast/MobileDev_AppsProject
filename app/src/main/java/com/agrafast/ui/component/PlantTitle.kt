package com.agrafast.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlantTitle(title : String, subtitle: String) {
  Surface(
    color = MaterialTheme.colorScheme.secondaryContainer,
//    tonalElevation = 4.dp
  ) {
    Row(
      modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 4.dp)
        .height(64.dp)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(verticalArrangement = Arrangement.Center) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleLarge,
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodyLarge,
        )
      }
    }
  }
}