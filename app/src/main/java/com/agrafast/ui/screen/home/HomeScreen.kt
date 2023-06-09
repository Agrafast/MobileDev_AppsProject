package com.agrafast.ui.screen.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.data.firebase.model.Plant
import com.agrafast.data.firebase.model.User
import com.agrafast.domain.UIState
import com.agrafast.domain.model.ElevationLevel
import com.agrafast.domain.model.LatLong
import com.agrafast.rememberAppState
import com.agrafast.ui.component.StatusComp
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.AuthViewModel
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme
import com.agrafast.ui.theme.Gray100
import com.agrafast.ui.theme.Gray400
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.android.gms.location.LocationServices


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
  appState: AppState,
  sharedViewModel: GlobalViewModel,
  authViewModel: AuthViewModel,
  context: Context = LocalContext.current
) {
  val permissions = listOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
  )
  val fusedLocationService = LocationServices.getFusedLocationProviderClient(context)

  // State
  val tutorialPlantsState = sharedViewModel.tutorialPlantsState.collectAsState()
  val currentLocation = remember { mutableStateOf<LatLong?>(null) }
  val user: User = remember { authViewModel.getUser() }


  // Function{
  fun getLocation() {
    fusedLocationService.lastLocation.addOnSuccessListener {
      it?.let {
        val latLong = LatLong(it.latitude, it.longitude)
        currentLocation.value = latLong
        authViewModel.getUserElevation(latLong)
      }
    }
  }

  // State, need to put last
  val multiplePermissionState =
    rememberMultiplePermissionsState(permissions = permissions, onPermissionsResult = {
      if (it[permissions[0]] == true && it[permissions[0]] == true) {
        getLocation()
      }
    })

  // SideEffects
  LaunchedEffect(Unit) {
    sharedViewModel.fetchTutorialPlants()
    multiplePermissionState.launchMultiplePermissionRequest()
  }
  LaunchedEffect(multiplePermissionState) {
    if (multiplePermissionState.allPermissionsGranted) {
      getLocation()
    }
  }

  LazyColumn(
    contentPadding = PaddingValues(bottom = 16.dp)
  ) {
    var locationName: String? = null
    if (currentLocation.value != null) {
      locationName = authViewModel.getLocationName(currentLocation.value!!, context)
    }
    item {

      UserInfo(
        user = user,
        locationName = locationName,
        canAccessLocation = multiplePermissionState.allPermissionsGranted
      )
    }
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
      val elevationText = if (authViewModel.userElevationState == ElevationLevel.BOTH) {
//        "Rekomendasi tanaman di daeerah anda."
        null
      }
//      else if (authViewModel.userElevationState == ElevationLevel.HIGH) {
//        "Rekomendasi tanaman dataran tinggi."
//      }
      else {
        "Rekomendasi yang cocok di daerah anda."
      }
      SectionTitle(
        text = stringResource(id = R.string.plant_stuff_title),
        subtitle = "Rekomendasi yang cocok di daerah anda.",
        showMore = true,
        onClickMore = { appState.navController.navigate(Screen.PlantList.to(authViewModel.userElevationState.level)) })
    }
    item {
      PlantStuffComp(
        elevationLevel = authViewModel.userElevationState,
        plantsState = tutorialPlantsState.value,
        onClickItem = {
          sharedViewModel.setCurrentTutorialPlant(it)
          appState.navController.navigate(route = Screen.PlantDetail.route)
        })
    }
  }
}

@Composable
fun UserInfo(
  modifier: Modifier = Modifier,
  user: User,
  locationName: String? = null,
  canAccessLocation: Boolean = false
) {
  Row(
    modifier = modifier
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
        text = locationName ?: "Mohon izinkan akses lokasi",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1
      )
    }
  }

}

@Composable
fun SectionTitle(
  text: String,
  subtitle: String? = null,
  showMore: Boolean = false,
  onClickMore: () -> Unit = {}
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(end = 16.dp, start = 16.dp, top = 20.dp, bottom = 8.dp),
    verticalArrangement = Arrangement.Center
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth(),
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
    if (subtitle != null) {
      Text(
        text = subtitle,
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
  val isImageLoadError: MutableState<Boolean?> = rememberSaveable { mutableStateOf(null) }
  Box(
    modifier = Modifier
      .height(height = height)
      .clip(RoundedCornerShape(12.dp))
      .fillMaxWidth()
      .background(Gray100)
      .clickable {
        onClickItem(plant)
      }
  ) {
    if (isImageLoadError.value != true) {
      AsyncImage(
        model = plant.image_url,
        contentDescription = plant.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxSize()
          .fillMaxHeight()
          .placeholder(
            visible = isImageLoadError.value == null,
            highlight = PlaceholderHighlight.shimmer(),
          ),
        onError = {
          isImageLoadError.value = true
        },
        onSuccess = {
          isImageLoadError.value = false
        }
      )
    } else {
      Image(
        painter = painterResource(id = R.drawable.ic_gallery_broken),
        modifier = Modifier
          .size(96.dp)
          .padding(16.dp)
          .align(Alignment.Center),
        contentDescription = null,
        colorFilter = ColorFilter.tint(Gray400)
      )
    }
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          brush = Brush.verticalGradient(
            listOf(
              Color.Black, Color.Transparent
            ),
            startY = Float.POSITIVE_INFINITY,
            endY = height.value - 32f
          )
        )
    ) {
      Text(
        modifier = Modifier
          .padding(horizontal = 8.dp, vertical = 4.dp)
          .align(
            Alignment.BottomStart
          ),
        text = plant.title,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White
      )
    }
  }
}


@Composable
fun PlantStuffComp(
  elevationLevel: ElevationLevel = ElevationLevel.BOTH,
  plantsState: UIState<List<Plant>>,
  onClickItem: (Plant) -> Unit
) {
  if (plantsState is UIState.Success) {
    val plants = plantsState.data!!
    val plantsToShow = mutableListOf<Plant>()
    if (elevationLevel != ElevationLevel.BOTH) {
      val filteredBoth = plants.filter { it.elevation == ElevationLevel.BOTH.level }
      val filteredOne = plants.filter {
        it.elevation == elevationLevel.level
      }
      plantsToShow.addAll(filteredOne)
      plantsToShow.addAll(filteredBoth)
    } else {
      plantsToShow.addAll(plants)
    }
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
      items(plantsToShow.subList(0, 6)) {
        PlantCard(plant = it, onClickItem = onClickItem)
      }
    }
  } else {
    StatusComp(state = plantsState)
  }
}

@Composable
fun PlantCard(plant: Plant, onClickItem: (Plant) -> Unit) {
  val isImageLoadError: MutableState<Boolean?> = rememberSaveable { mutableStateOf(null) }
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(160.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Gray100)
        .clickable {
          onClickItem(plant)
        }
    ) {
      if (isImageLoadError.value != true) {
        AsyncImage(
          model = plant.image_url,
          contentDescription = plant.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxSize()
            .placeholder(
              visible = isImageLoadError.value == null,
              highlight = PlaceholderHighlight.shimmer(),
            ),
          onLoading = {
            isImageLoadError.value = null
          },
          onError = {
            isImageLoadError.value = true
          },
          onSuccess = {
            isImageLoadError.value = false
          }
        )
      } else {
        Image(
          painter = painterResource(id = R.drawable.ic_gallery_broken),
          modifier = Modifier
            .align(Alignment.Center)
            .size(64.dp)
            .padding(16.dp),
          contentDescription = null,
          colorFilter = ColorFilter.tint(Gray400)
        )
      }
    }
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
    val authViewModel: AuthViewModel = viewModel()
    HomeScreen(rememberAppState(), viewModel, authViewModel)
  }
}