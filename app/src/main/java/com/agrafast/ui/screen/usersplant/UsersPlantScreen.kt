package com.agrafast.ui.screen.usersplant

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.agrafast.R
import com.agrafast.ui.component.PlantList
import com.agrafast.ui.component.SimpleTopBar
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme

@Composable
fun UsersPlantsScreen(
  navController: NavController,
  sharedViewModel: GlobalViewModel,
) {
  Surface {
    val plantsState = sharedViewModel.myPlants.collectAsState()
    Column {
      SimpleTopBar(stringResource(id = R.string.my_plant))
      PlantList(
        plants = plantsState.value,
        onItemClick = {
          sharedViewModel.setCurrentTutorialPlant(it)
          navController.navigate(route = Screen.PlantDetail.route)
        },
        onDismiss = { plant ->
          sharedViewModel.removeMyPlant(plant)
        })
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    val viewModel: GlobalViewModel = viewModel()
    UsersPlantsScreen(rememberNavController(), viewModel)
  }
}