package com.agrafast.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.agrafast.R
import com.agrafast.data.firebase.model.Plant
import com.agrafast.domain.UIState
import com.agrafast.ui.theme.AgraFastTheme
import com.agrafast.ui.theme.Gray200
import com.agrafast.ui.theme.Gray600


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlantList(
  plants: List<Plant>,
  onItemClick: (Plant) -> Unit,
  onDismiss: ((plant: Plant, index: Int) -> Unit)? = null,
) {
  if (plants.isNotEmpty()) {
    LazyColumn(
      modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      itemsIndexed(plants, key = { _, it -> it.id }) { index, plant ->
        if (onDismiss == null) {
          PlantListItem(
            modifier = Modifier.padding(horizontal = 16.dp),
            plant = plant,
            onClick = onItemClick
          )
        } else {
          SwipeablePlantListItem(
            modifier = Modifier.animateItemPlacement(),
            plant = plant,
            index = index,
            onItemClick = onItemClick,
            onDismiss = onDismiss
          )
        }
      }
      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  } else {
    StatusComp(UIState.Empty)
  }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeablePlantListItem(
  modifier: Modifier,
  plant: Plant,
  index: Int,
  onItemClick: (Plant) -> Unit,
  onDismiss: (plant: Plant, index: Int) -> Unit
) {
  val dismissState = rememberDismissState(positionalThreshold = {
    100.dp.toPx()
  }, confirmValueChange = { dismissValue ->
    if (dismissValue == DismissValue.DismissedToEnd) onDismiss(plant, index)
    true
  })
  SwipeToDismiss(
    modifier = modifier
      .padding(horizontal = 16.dp)
      .clip(RoundedCornerShape(8.dp)),
    state = dismissState,
    directions = setOf(DismissDirection.StartToEnd),
    background = {
      val color = animateColorAsState(
        when (dismissState.targetValue) {
          DismissValue.Default -> Gray200
          DismissValue.DismissedToEnd -> Color.Red
          else -> Color.Transparent
        }
      )
      val colorIcon = animateColorAsState(
        when (dismissState.targetValue) {
          DismissValue.DismissedToEnd -> Color.White
          else -> Gray600
        }
      )
      Box(
        Modifier
          .fillMaxSize()
          .background(color = color.value)
          .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_trash),
          contentDescription = "Delete",
          tint = colorIcon.value
        )
      }
    },
    dismissContent = {
      PlantListItem(plant = plant, onClick = onItemClick)
    })
}

@Composable
fun PlantListItem(modifier: Modifier = Modifier, plant: Plant, onClick: (Plant) -> Unit) {
  Surface(
    tonalElevation = 4.dp,
    modifier = modifier
      .height(96.dp)
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .clickable {
        onClick(plant)
      },

    ) {
    Row() {
      AsyncImage(
        modifier = Modifier
          .size(96.dp)
          .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop,
        model = plant.image_url,
        contentDescription = null
      )
      Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        Text(
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          text = plant.title,
          style = MaterialTheme.typography.titleSmall,
        )
        Text(
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          text = plant.botanical_name,
          style = MaterialTheme.typography.labelLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Selengkapnya tentang ${plant.title}",
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PlantListItemPreview() {
  AgraFastTheme {
    PlantListItem(plant = Plant(), onClick = {})
  }
}