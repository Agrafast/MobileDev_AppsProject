package com.agrafast.ui.screen.usersplant

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.data.firebase.model.Plant
import com.agrafast.domain.UIState
import com.agrafast.rememberAppState
import com.agrafast.ui.component.PlantList
import com.agrafast.ui.component.SimpleTopBar
import com.agrafast.ui.component.StatusComp
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme

@Composable
fun UserPlantListScreen(
  appState: AppState,
  sharedViewModel: GlobalViewModel,
  viewModel: PlantListViewModel = hiltViewModel()
) {

  val userPlantsState = viewModel.userPlantsState.collectAsState()

  LaunchedEffect(Unit) {
    viewModel.getUserPlants(appState.user.id)
  }
  Surface {
    Column(
    ) {
      SimpleTopBar(stringResource(id = R.string.my_plant))
      if (userPlantsState.value is UIState.Success<List<Plant>>) {
        val plants = (userPlantsState.value as UIState.Success<List<Plant>>).data!!
        PlantList(
          plants = plants,
          onItemClick = {
            sharedViewModel.setCurrentTutorialPlant(it)
            appState.navController.navigate(route = Screen.PlantDetail.route)
          },
          onDismiss = { plant ->
            viewModel.deleteFromUserPlant(appState = appState, plantId = plant.id)
          })
      } else {
        StatusComp(state = userPlantsState.value)
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    val viewModel: GlobalViewModel = viewModel()
    UserPlantListScreen(rememberAppState(), viewModel)
  }
}