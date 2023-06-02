package com.agrafast.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.agrafast.R
import com.agrafast.domain.model.Plant
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme


@Composable
fun HomeScreen(
  navController: NavController,
  sharedViewModel: GlobalViewModel,
) {
//  Surface(
//  ) {
  LazyColumn(
    contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
  ) {
    item { UserInfo() }
    item { SectionTitle(text = stringResource(id = R.string.disease_detector_title)) }
    item { DiseaseDetectionComp(_plants = sharedViewModel.getDummyDiseasePlants()) }
    item {
      SectionTitle(
        text = stringResource(id = R.string.plant_stuff_title), showMore = true,
        onClickMore = { navController.navigate(Screen.PlantList.route) })
    }
    item {
      PlantStuffComp(
        plants = sharedViewModel.getDummyTutorialPlants(),
        onClickItem = {
          sharedViewModel.setCurrentTutorialPlant(it)
          navController.navigate(route = Screen.PlantDetail.route)
        })
    }
  }
//  }
}

@Composable
fun UserInfo() {
  Row(
    modifier = Modifier.height(64.dp).padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(64.dp)
        .background(color = MaterialTheme.colorScheme.surfaceVariant, CircleShape)
    ) {
      Icon(
        Icons.Outlined.Person,
        contentDescription = null,
        modifier = Modifier
          .fillMaxSize()
          .padding(8.dp)
      )
    }
    Spacer(modifier = Modifier.width(16.dp))
    Column(
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = "Indi nih Boss",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.fillMaxWidth()
      )
      Text(
        text = "Senggol dongg!!!!!",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }

}

@Composable
fun SectionTitle(text: String, showMore: Boolean = false, onClickMore: () -> Unit = {}) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(end = 16.dp, start = 16.dp, top = 20.dp, bottom = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.titleLarge,
      maxLines = 1,
    )

    if (showMore) {
      Text(
        modifier = Modifier.clickable(onClick = onClickMore),
        text = "Selengkapnya",
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
      )
    }
  }
}

@Composable
fun DiseaseDetectionComp(_plants: List<Plant>) {
  val plants = remember { _plants.shuffled() }
  Column(
    Modifier.padding(horizontal = 16.dp)
  ) {
    DiseaseDetectionPlantCard(plant = plants[0], height = 192.dp)
    Spacer(modifier = Modifier.height(16.dp))
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(112.dp)
    )
    {
      items(plants.subList(1, plants.size)) {
        DiseaseDetectionPlantCard(plant = it, height = 112.dp)
      }
    }
  }
}

@Composable
fun DiseaseDetectionPlantCard(plant: Plant, height: Dp) {
  Box(
    modifier = Modifier
      .height(height = height)
      .clip(RoundedCornerShape(12.dp))
      .fillMaxWidth()
  ) {
    Image(
      painter = painterResource(id = plant.image),
      contentDescription = plant.title,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxSize()
        .fillMaxHeight()
    )
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          brush = Brush.verticalGradient(
            listOf(
              Color.Black, Color.Transparent
            ),
            startY = Float.POSITIVE_INFINITY,
            endY = height.value - 64f
          )
        )
    ) {
      Text(
        modifier = Modifier
          .padding(horizontal = 8.dp, vertical = 4.dp)
          .align(
            Alignment
              .BottomStart
          ),
        text = plant.title,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White
      )
    }
  }
}


@Composable
fun PlantStuffComp(plants: List<Plant>, onClickItem: (Plant) -> Unit) {
  val halfNumber: Int = plants.size / 2
  val plantsA = plants.subList(0, halfNumber)
  val plantsB = plants.subList(halfNumber, plants.size)
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(horizontal = 16.dp)
  ) {
    items(plantsA) {
      PlantCard(plant = it, onClickItem = onClickItem)
    }
  }
  Spacer(modifier = Modifier.height(8.dp))
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(horizontal = 16.dp)
  ) {
    items(plantsB) {
      PlantCard(plant = it, onClickItem = onClickItem)
    }
  }
}

@Composable
fun PlantCard(plant: Plant, onClickItem: (Plant) -> Unit) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Image(
      painter = painterResource(id = plant.image),
      contentDescription = plant.title,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .size(160.dp)
        .clip(RoundedCornerShape(16.dp))
        .clickable {
          onClickItem(plant)
        }
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      modifier = Modifier.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
      ) {
        onClickItem(plant)
      },
      text = plant.title,
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    val viewModel: GlobalViewModel = viewModel()
    HomeScreen(rememberNavController(), viewModel)
  }
}