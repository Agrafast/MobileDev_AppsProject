package com.agrafast.ui.screen.plant

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.domain.model.ElevationLevel
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
  elevationLevel: ElevationLevel = ElevationLevel.BOTH
) {
  val plants = sharedViewModel.getTutorialPlants()
  var plantsState by remember { mutableStateOf(plants) }
  val searchValue: MutableState<String> = remember { mutableStateOf("") }
  val selectedLevel: MutableState<ElevationLevel> = remember { mutableStateOf(elevationLevel) }


  LaunchedEffect(searchValue.value, selectedLevel.value) {
    var filtered = plants
    if (selectedLevel.value.level != ElevationLevel.BOTH.level) {
      filtered = filtered.filter { it.elevation == selectedLevel.value.level }
    }
    plantsState = filtered.filter { plant ->
      plant.title.contains(
        searchValue.value,
        true
      ) or plant.botanical_name.contains(searchValue.value, true)
    }
    Log.d("TAG", "PlantListScreen: filtered ${filtered.size}")
  }
  Surface {
    Column {
      SimpleActionBar(
        title = stringResource(id = R.string.plants),
        onBackClicked = { appState.navController.navigateUp() })
      Spacer(modifier = Modifier.height(8.dp))
      SearchBox(
        value = searchValue.value,
        elevationLevel = selectedLevel.value,
        onValueChange = {
          searchValue.value = it

        },
        onDropDownSelected = {
          selectedLevel.value = it
        }
      )
      Spacer(modifier = Modifier.height(16.dp))
      PlantList(plantsState, onItemClick = {
        sharedViewModel.setCurrentTutorialPlant(it)
        appState.navController.navigate(route = Screen.PlantDetail.route)
      })
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(
  elevationLevel: ElevationLevel,
  value: String,
  onValueChange: (String) -> Unit,
  onDropDownSelected: (ElevationLevel) -> Unit
) {
  var expanded by remember { mutableStateOf(false) }

  fun getElevationText(elevationLevel: ElevationLevel): String {
    return when (elevationLevel.level) {
      1 -> "Dataran rendah"
      2 -> "Dataran tinggi"
      else -> "Semua"
    }
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
  ) {
    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth(),
      value = value,
      onValueChange = onValueChange,
      placeholder = {
        Text(text = stringResource(id = R.string.search_plant))
      },
      trailingIcon = {
        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
      }
    )
    Spacer(modifier = Modifier.height(8.dp))
    ExposedDropdownMenuBox(
      modifier = Modifier
        .fillMaxWidth(),
      expanded = expanded, onExpandedChange = {
        expanded = !expanded
      }) {
      TextField(
        value = getElevationText(elevationLevel),
        onValueChange = {},
        readOnly = true,
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        modifier = Modifier
          .menuAnchor()
          .fillMaxWidth()
      )

      ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        ElevationLevel.values().forEach {
          DropdownMenuItem(text = { Text(text = getElevationText(it)) }, onClick = {
            expanded = false
            onDropDownSelected(it)
          })
        }
      }
    }
  }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    val viewModel: GlobalViewModel = viewModel()
    PlantListScreen(rememberAppState(), viewModel)
  }
}