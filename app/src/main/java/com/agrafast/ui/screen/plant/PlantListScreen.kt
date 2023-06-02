package com.agrafast.ui.screen.plant

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.agrafast.R
import com.agrafast.ui.component.SimpleActionBar
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.screen.detail.PlantingTutorial
import com.agrafast.ui.theme.AgraFastTheme

@Composable
fun PlantListScreen(
  navController: NavController,
  sharedViewModel: GlobalViewModel,
) {

  val plantsState = remember { mutableStateOf(sharedViewModel.tutorialPlant) }
  val searchValue = remember { mutableStateOf("") }
  Log.d("TAG", "PlantListScreen: Recomposition")
  Surface() {
    Column {
      SimpleActionBar(
        title = stringResource(id = R.string.plants),
        onBackClicked = { navController.navigateUp() })
      Spacer(modifier = Modifier.width(8.dp))
      SearchBox(
        value = searchValue.value, onValueChange = { searchValue.value = it },
      )
      Spacer(modifier = Modifier.width(8.dp))
      PlantList()
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

@Composable
fun PlantList(){

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    val viewModel: GlobalViewModel = viewModel()
    PlantListScreen(rememberNavController(), viewModel)
  }
}