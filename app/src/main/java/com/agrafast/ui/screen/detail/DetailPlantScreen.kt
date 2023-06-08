package com.agrafast.ui.screen.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.data.firebase.model.Plant
import com.agrafast.data.firebase.model.TutorialStep
import com.agrafast.domain.UIState
import com.agrafast.rememberAppState
import com.agrafast.ui.component.ExpandableWithDivider
import com.agrafast.ui.component.PlantTitle
import com.agrafast.ui.component.SimpleActionBar
import com.agrafast.ui.component.StatusComp
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme
import com.agrafast.ui.theme.Gray200
import com.agrafast.util.formatWithOrdered

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlantDetailScreen(
  appState: AppState,
  sharedViewModel: GlobalViewModel,
) {
  val viewModel = hiltViewModel<DetailPlantViewModel>()
  val plant: Plant = sharedViewModel.tutorialPlant!!

//  State
  val tutorialStepsState = viewModel.tutorialsState.collectAsState()
  val isInUserPlantState = viewModel.isInUserPlantState.collectAsState()
  // SideEffects
  LaunchedEffect(Unit) {
    viewModel.getPlantTutorial(plant.id)
    viewModel.checkIsInUserPlant(appState.user.id, plant.id)
  }


  Surface(
    modifier = Modifier.fillMaxHeight()
  ) {
    LazyColumn(
    ) {
      // TODO -> Change to Network Image (AsyncImage)
      item {
        PlantImageComp(
          imageModel = plant.image_url,
          onBackClicked = { appState.navController.navigateUp() })
      }
      stickyHeader {
        PlantTitle(
          plant.title,
          plant.botanical_name,
          isInMyPlant = isInUserPlantState.value,
          showDetection = sharedViewModel.getShowDetection(plant.name),
          onDetectClick = {
            sharedViewModel.setCurrentDetectionPlant(plant)
            appState.navController.navigate(Screen.PlantDiseaseDetection.route)
          },
          onToMyPlantClick = {
            if (isInUserPlantState.value) {
              viewModel.deleteFromUserPlant(appState, plantId = plant.id)
            } else {
              viewModel.insertToUserPlant(appState, plantId = plant.id)
            }
          })
      }
      item {
        PlantingTutorial(tutorialStepsState)
      }
    }
  }
}

@Composable
fun PlantImageComp(imageModel: Any, onBackClicked: () -> Unit) {
  Surface(color = MaterialTheme.colorScheme.secondaryContainer) {
    Box(
      modifier = Modifier
        .height(320.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
        .background(Gray200)
    ) {
      AsyncImage(
        model = imageModel,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxSize()
          .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
      )
      SimpleActionBar(
        title = stringResource(id = R.string.plant_tutorial),
        onBackClicked = onBackClicked,
        isBackgroundTransparent = true
      )
    }
  }
}

@Composable
fun PlantingTutorial(tutorialStepsState: State<UIState<List<TutorialStep>>>) {
  if (tutorialStepsState.value is UIState.Success<List<TutorialStep>>) {
    val tutorialSteps =
      (tutorialStepsState.value as UIState.Success<List<TutorialStep>>).data!!.sortedBy { it.tahap_id }
    Column(
    ) {
      Surface(
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
          text = "Langkah - langkah : ",
          style = MaterialTheme.typography.titleMedium,
        )
      }
      tutorialSteps.mapIndexed { index, it ->
        ExpandableWithDivider(
          title = "${it.tahap_id}. ${it.tahapan_menanam}",
          description = it.detail_kegiatan.formatWithOrdered(),
          defaultState = index == 0,
          isLast = index > tutorialSteps.size - 2
        )
      }
    }
  } else {
    StatusComp(state = tutorialStepsState.value)
  }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    val viewModel: GlobalViewModel = viewModel()
    PlantDetailScreen(rememberAppState(), viewModel)
  }
}