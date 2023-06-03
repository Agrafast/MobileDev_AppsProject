package com.agrafast.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.agrafast.domain.model.Plant
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme
import com.agrafast.util.TextUtil


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlantList(
  plants: List<Plant>,
  onItemClick: (Plant) -> Unit,
  onDismiss: ((plant: Plant) -> Unit)? = null,
) {
  if (plants.isNotEmpty()) {
    LazyColumn(
      modifier = Modifier.fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(
        plants,
        key = { it.hashCode() }
      ) {
        if (onDismiss == null) {
          PlantListItem(plant = it, onClick = onItemClick)
        } else {

          SwipeablePlantListItem(
            modifier = Modifier.animateItemPlacement(),
            plant = it,
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
    StatusComp()
  }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeablePlantListItem(
  modifier: Modifier,
  plant: Plant,
  onItemClick: (Plant) -> Unit,
  onDismiss: (plant: Plant) -> Unit
) {
  val dismissState = rememberDismissState(
    confirmValueChange = { dismissValue ->
      if (dismissValue == DismissValue.DismissedToEnd) onDismiss(plant)
      true
    }
  )
  SwipeToDismiss(
    modifier = modifier,
    state = dismissState,
    directions = setOf(DismissDirection.StartToEnd),
    background = {},
    dismissContent = {
      PlantListItem(plant = plant, onClick = onItemClick)
    }
  )
}

@Composable
fun PlantListItem(plant: Plant, onClick: (Plant) -> Unit) {
  Row(
    modifier = Modifier
      .padding(horizontal = 16.dp)
      .height(96.dp)
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(MaterialTheme.colorScheme.inverseOnSurface)
      .clickable {
        onClick(plant)
      },
  ) {
    Image(
      modifier = Modifier
        .size(96.dp)
        .clip(RoundedCornerShape(8.dp)),
      contentScale = ContentScale.Crop,
      painter = painterResource(id = plant.image), contentDescription = null
    )
    Column(
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
      Text(
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        text = TextUtil.buildPlantNameWithLatin(plant),
        style = MaterialTheme.typography.labelLarge,
      )
      Text(
        text = plant.description,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PlantListItemPreview() {
  AgraFastTheme {
    PlantListItem(plant = GlobalViewModel().getDummyTutorialPlants(1)[0], {})
  }
}