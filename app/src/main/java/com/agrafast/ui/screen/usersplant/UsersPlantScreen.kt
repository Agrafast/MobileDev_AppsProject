package com.agrafast.ui.screen.usersplant

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.data.firebase.model.Plant
import com.agrafast.domain.UIState
import com.agrafast.rememberAppState
import com.agrafast.ui.component.SimpleTopBar
import com.agrafast.ui.component.StatusComp
import com.agrafast.ui.component.SwipeablePlantListItem
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.AuthViewModel
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserPlantListScreen(
  appState: AppState,
  sharedViewModel: GlobalViewModel,
  authViewModel: AuthViewModel,
  viewModel: PlantListViewModel = hiltViewModel()
) {

  val userPlantsState = viewModel.userPlantsState.collectAsState()
  val user = authViewModel.getUser()

  LaunchedEffect(Unit) {
    viewModel.getUserPlants(userId = user.id)
  }
  Surface {
    LazyColumn(
      modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      stickyHeader { SimpleTopBar(stringResource(id = R.string.my_plant)) }
      if (userPlantsState.value is UIState.Success<List<Plant>>) {
        val plants = (userPlantsState.value as UIState.Success<List<Plant>>).data!!
        itemsIndexed(plants, key = { _, it -> it.id }) { index, plant ->
          SwipeablePlantListItem(
            modifier = Modifier.animateItemPlacement(),
            plant = plant,
            index = index,
            onItemClick = {
              sharedViewModel.setCurrentTutorialPlant(it)
              appState.navController.navigate(route = Screen.PlantDetail.route)
            },
            onDismiss = { pPlant, pIndex ->
              viewModel.deleteFromUserPlant(
                userId = user.id,
                plant = pPlant,
                pIndex,
                appState = appState,
              )
            }
          )
        }
      } else {
        item {
          StatusComp(state = userPlantsState.value)
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    UserPlantListScreen(rememberAppState(), hiltViewModel(), hiltViewModel())
  }
}