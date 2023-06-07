package com.agrafast.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.agrafast.R

@Composable
fun PlantTitle(
  title: String,
  subtitle: String,
  isInMyPlant: Boolean = false,
  showDetection: Boolean = false,
  onDetectClick: () -> Unit = {},
  onToMyPlantClick: () -> Unit = {}
) {

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
      Column(
        verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleLarge,
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodyLarge,
        )
      }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        val myPlantRes =
          if (isInMyPlant) R.drawable.ic_plant else R.drawable.ic_plant_outline
        val myPlantTint =
          if (isInMyPlant) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
        if(showDetection){
          Image(
            modifier = Modifier
              .size(32.dp)
              .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
              ) { onDetectClick() },
            painter = painterResource(id = R.drawable.ic_scan),
            contentDescription = "Deteksi penyakit",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
          )
        }
        Image(
          modifier = Modifier
            .size(32.dp)
            .clickable(
              indication = null,
              interactionSource = MutableInteractionSource()
            ) { onToMyPlantClick() },
          painter = painterResource(id = myPlantRes),
          contentDescription = "Tambah ke tanamnaku",
          colorFilter = ColorFilter.tint(myPlantTint)
        )
      }
    }
  }
}