package com.agrafast.ui.screen.plant

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.data.firebase.model.Plant
import com.agrafast.domain.UIState
import com.agrafast.rememberAppState
import com.agrafast.ui.component.PlantList
import com.agrafast.ui.component.SimpleActionBar
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme

@Composable
fun PlantListScreen(
  appState: AppState,
  sharedViewModel: GlobalViewModel,
) {
  val plants = sharedViewModel.getTutorialPlants()
  var plantsState = remember{plants}
  var searchValue by remember { mutableStateOf("") }

  Log.d("TAG", "PlantListScreen: Recomposition")
  Surface {
    Column {
      SimpleActionBar(
        title = stringResource(id = R.string.plants),
        onBackClicked = { appState.navController.navigateUp() })
      Spacer(modifier = Modifier.height(8.dp))
      SearchBox(
        value = searchValue,
        onValueChange = {
          searchValue = it
          plantsState = plants.filter { plant ->
            plant.title.contains(it, true) or plant.botanical_name.contains(it, true)
          }
        },
      )
      Spacer(modifier = Modifier.height(16.dp))
      PlantList(plantsState, onItemClick = {
        sharedViewModel.setCurrentTutorialPlant(it)
        appState.navController.navigate(route = Screen.PlantDetail.route)
      })
    }
  }
}

@Composable
fun SearchBox(value: String, onValueChange: (String) -> Unit) {
  OutlinedTextField(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    value = value,
    onValueChange = onValueChange,
    placeholder = {
      Text(text = stringResource(id = R.string.search_plant))
    },
    trailingIcon = {
      Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
    }
  )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    val viewModel: GlobalViewModel = viewModel()
    PlantListScreen(rememberAppState(), viewModel)
  }
}