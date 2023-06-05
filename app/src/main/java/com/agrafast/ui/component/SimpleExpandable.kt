package com.agrafast.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Composable
fun SimpleExpandable(
  title: String,
  description: String,
  defaultState: Boolean = false,
) {
  var expandedState by remember { mutableStateOf(defaultState) }
  val rotationState by animateFloatAsState(
    targetValue = if (expandedState) 180f else 0f
  )
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .animateContentSize(animationSpec = tween(300, easing = LinearOutSlowInEasing))
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .clickable(
          indication = null,
          interactionSource = MutableInteractionSource()
        ) { expandedState = !expandedState }
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
      )
      Spacer(modifier = Modifier.width(4.dp))
      Icon(
        modifier = Modifier
          .rotate(rotationState)
          .clickable(
            indication = null,
            interactionSource = MutableInteractionSource()
          ) { expandedState = !expandedState },
        imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Drop-Down Arrow"
      )
    }
    if (expandedState) {
      Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
      ) //

    }
  }
}