package com.agrafast.ui.screen.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
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
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme

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

  // SideEffects
  DisposableEffect(Unit) {
    viewModel.getPlantTutorial(plant.id)
    onDispose {
      sharedViewModel.setCurrentTutorialPlant(null)
    }
  }

  Surface(
    modifier = Modifier.fillMaxHeight()
  ) {
    LazyColumn(
    ) {
      // TODO -> Change to Network Image (AsyncImage)
      item {
        Box {
          AsyncImage(
            model = plant.image_url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .fillMaxWidth()
              .height(320.dp)
              .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
          )
          SimpleActionBar(
            title = stringResource(id = R.string.plant_tutorial),
            onBackClicked = { appState.navController.navigateUp() },
            isBackgroundTransparent = true
          )
        }
      }
      stickyHeader {
        PlantTitle(plant.title, plant.botanical_name)
      }
      item {
        PlantingTutorial(tutorialStepsState)
      }
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
          modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
          text = "Langkah - langkah : ",
          style = MaterialTheme.typography.titleMedium,
        )
      }
      tutorialSteps.mapIndexed { index, it ->
        ExpandableWithDivider(
          title = "${it.tahap_id}. ${it.tahapan_menanam}",
          description = it.detail_kegiatan,
          isLast = index > tutorialSteps.size - 2
        )
      }
    }
  } else {
    StatusComp(state = tutorialStepsState.value)
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
    PlantDetailScreen(rememberAppState(), viewModel)
  }
}