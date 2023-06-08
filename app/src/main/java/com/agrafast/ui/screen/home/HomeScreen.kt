package com.agrafast.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.domain.UIState
import com.agrafast.data.firebase.model.Plant
import com.agrafast.data.firebase.model.User
import com.agrafast.rememberAppState
import com.agrafast.ui.component.StatusComp
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme


@Composable
fun HomeScreen(
  appState : AppState,
  sharedViewModel: GlobalViewModel,
//  viewModel: HomeViewModel = hiltViewModel()
) {

  val tutorialPlantsState = sharedViewModel.tutorialPlantsState.collectAsState()

  // SideEffects
  LaunchedEffect(Unit){
    sharedViewModel.fetchTutorialPlants()
  }
  LazyColumn(
    contentPadding = PaddingValues(bottom = 16.dp)
  ) {
    item { UserInfo(appState.user) }
    item { SectionTitle(text = stringResource(id = R.string.disease_detector_title)) }
    item {
      DiseaseDetectionComp(
        plants = sharedViewModel.fetchPredictionPlants(),
        onClickItem = {
          sharedViewModel.setCurrentDetectionPlant(it)
          appState.navController.navigate(route = Screen.PlantDiseaseDetection.route)
        })
    }
    item {
      SectionTitle(
        text = stringResource(id = R.string.plant_stuff_title), showMore = true,
        onClickMore = { appState.navController.navigate(Screen.PlantList.route) })
    }
    item {
      PlantStuffComp(
        plantsState = tutorialPlantsState.value,
        onClickItem = {
          sharedViewModel.setCurrentTutorialPlant(it)
          appState.navController.navigate(route = Screen.PlantDetail.route)
        })
    }
  }
}

@Composable
fun UserInfo(user: User) {
  Row(
    modifier = Modifier
      .padding(vertical = 16.dp, horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Surface(
      modifier = Modifier.size(48.dp),
      tonalElevation = 4.dp,
      shape = CircleShape
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
        text = user.name,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.fillMaxWidth()
      )
      Text(
        text = user.email,
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
fun DiseaseDetectionComp(plants: List<Plant>, onClickItem: (Plant) -> Unit) {
  val shuffledPlants = remember { plants.shuffled() }
  Column(
    Modifier.padding(horizontal = 16.dp)
  ) {
    DiseaseDetectionPlantCard(plant = shuffledPlants[0], height = 192.dp, onClickItem = onClickItem)
    Spacer(modifier = Modifier.height(16.dp))
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(112.dp)
    )
    {
      items(shuffledPlants.subList(1, plants.size)) {
        DiseaseDetectionPlantCard(plant = it, height = 112.dp, onClickItem = onClickItem)
      }
    }
  }
}

@Composable
fun DiseaseDetectionPlantCard(plant: Plant, height: Dp, onClickItem: (Plant) -> Unit) {
  Box(
    modifier = Modifier
      .height(height = height)
      .clip(RoundedCornerShape(12.dp))
      .fillMaxWidth()
      .clickable {
        onClickItem(plant)
      }
  ) {
    AsyncImage(
      model = plant.image_url,
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
fun PlantStuffComp(plantsState: UIState<List<Plant>>, onClickItem: (Plant) -> Unit) {
  if (plantsState is UIState.Success) {
    val plants : List<Plant> = plantsState.data!!.subList(0,6)
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
      items(plants) {
        PlantCard(plant = it, onClickItem = onClickItem)
      }
    }
  } else {
    StatusComp(state = plantsState)
  }
}

@Composable
fun PlantCard(plant: Plant, onClickItem: (Plant) -> Unit) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    AsyncImage(
      model = plant.image_url,
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
    HomeScreen(rememberAppState(), viewModel)
  }
}