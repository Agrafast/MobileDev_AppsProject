package com.agrafast.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
    Surface(
      tonalElevation = 4.dp
    ) {
      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 16.dp)
          .clickable(
            indication = null,
            interactionSource = MutableInteractionSource()
          ) { expandedState = !expandedState }
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall,
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
    }
    if (expandedState) {
      Box(
        Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        Text(
          text = description,
          style = MaterialTheme.typography.bodyMedium,
        )
      } //

    }
  }
}

@Composable
fun ExpandableWithDivider(
  title: String,
  description: String,
  defaultState: Boolean = false,
  isLast: Boolean
) {
  Column {
    SimpleExpandable(title = title, description = description, defaultState = defaultState)
    if (!isLast) {
      Divider()
    }
  }
}