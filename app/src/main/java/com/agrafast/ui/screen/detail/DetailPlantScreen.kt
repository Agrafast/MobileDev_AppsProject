package com.agrafast.ui.screen.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agrafast.domain.model.Plant
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme

@Composable
fun PlantDetailScreen(
  sharedViewModel: GlobalViewModel,
) {
  val plant: Plant = sharedViewModel.tutorialPlant!!
  Surface {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(
          rememberScrollState()
        )
    ) {
      // TODO -> Change to Network Image (AsyncImage)
      Image(

        painter = painterResource(id = plant.image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .height(320.dp)
          .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
      )
      Spacer(modifier = Modifier.height(12.dp))
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = plant.title,
        style = MaterialTheme.typography.titleLarge,
      )
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = "Nama latin ni boss",
        style = MaterialTheme.typography.bodyLarge,
      ) // TODO Update based API
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = LoremIpsum(20).values.joinToString(""), // TODO From API
        style = MaterialTheme.typography.bodyMedium,
      ) // TODO Update based API
      Spacer(modifier = Modifier.height(16.dp))
      Divider()
      Spacer(modifier = Modifier.height(16.dp))
      PlantingTutorial()
      Spacer(modifier = Modifier.height(16.dp)) // For Spacing with NavBar
    }
  }
}


// TODO From API
@Composable
fun PlantingTutorial() {
  Column(modifier = Modifier.padding(horizontal = 16.dp)) {
    Text(
      text = "Cara Bertanam",
      style = MaterialTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(4.dp))
    ExpandableCard(title = "Pembibitan", description = LoremIpsum(40).values.joinToString(" "))
    ExpandableCard(title = "Penanaman", description = LoremIpsum(40).values.joinToString(" "))
    ExpandableCard(title = "Perawatan", description = LoremIpsum(40).values.joinToString(" "))
    ExpandableCard(title = "Maling", description = LoremIpsum(40).values.joinToString(" "))
    ExpandableCard(title = "Maling Lagi", description = LoremIpsum(40).values.joinToString(" "))
    ExpandableCard(title = "Panen Guys", description = LoremIpsum(40).values.joinToString(" "))
  }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableCard(
  title: String,
  description: String,

  ) {
  var expandedState by remember { mutableStateOf(false) }
  val rotationState by animateFloatAsState(
    targetValue = if (expandedState) 180f else 0f
  )
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .animateContentSize(animationSpec = tween(300, easing = LinearOutSlowInEasing))
      .clip(
        RoundedCornerShape(4.dp)
      )

  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier
        .fillMaxWidth()
        .background(
          MaterialTheme.colorScheme.secondaryContainer
        )
        .clickable { expandedState = !expandedState }
        .padding(horizontal = 8.dp),
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
      )
      IconButton(
        modifier = Modifier.rotate(rotationState),
        onClick = { expandedState = !expandedState }) {
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Drop-Down Arrow")
      }
    }
    if (expandedState) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .padding(horizontal = 8.dp, vertical = 8.dp)
      ) {
        Text(
          text = description, // TODO From API
          style = MaterialTheme.typography.bodyMedium,
        ) //
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    val viewModel: GlobalViewModel = viewModel()
    PlantDetailScreen(viewModel)
  }
}