package com.agrafast.ui.screen.detection

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.agrafast.AppState
import com.agrafast.BuildConfig
import com.agrafast.R
import com.agrafast.domain.UIState
import com.agrafast.data.firebase.model.PlantDisease
import com.agrafast.rememberAppState
import com.agrafast.ui.component.PlantTitle
import com.agrafast.ui.component.SimpleExpandable
import com.agrafast.ui.component.StatusComp
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme
import com.agrafast.ui.theme.Gray200
import com.agrafast.util.HEALTHY_NAME
import com.agrafast.util.createTempFile
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlantDiseaseDetectionScreen(
  appState: AppState,
  sharedViewModel: GlobalViewModel,
  setFabBehavior: (Boolean, @Composable () -> Unit, () -> Unit) -> Unit
) {
  val context = LocalContext.current
  val viewModel: PlantDiseaseDetectionViewModel = hiltViewModel()
  val plant = sharedViewModel.detectionPlant!!

  // Camera and Gallery Launcher Stufff
  val tempFile = context.createTempFile()
  val tempFileUri: Uri = FileProvider.getUriForFile(
    context,
    BuildConfig.APPLICATION_ID,
    tempFile
  )
  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture(),
    onResult = {
      if (it) {
        viewModel.setCurrentImage(tempFile.toUri())
      }
    })
  val imagePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
    onResult = {
      it?.let { viewModel.setCurrentImage(it) }
    })
  val permissionLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) {
    if (!it) {
      Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
    }
  }

  // State
  val diseaseListState = viewModel.plantDiseaseListState.collectAsState()
  val predictedDiseaseState = viewModel.predictedDiseaseState.collectAsState()
  val currentImageState = viewModel.currentImage.collectAsState()
  val cameraAllowed: MutableState<Boolean> = remember { mutableStateOf(false) }

  // SideEffects
  // Launch one time
  LaunchedEffect(Unit) {
    viewModel.getPlantDiseases(plant.id)

    // Check Permission 2 times
    val permissionCheckResult =
      ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
      cameraAllowed.value = true
    } else {
      permissionLauncher.launch(Manifest.permission.CAMERA)
    }
  }
  DisposableEffect(currentImageState.value) {
    val showFab = currentImageState.value != null
    setFabBehavior(showFab, {
      Icon(
        modifier = Modifier.size(48.dp),
        painter = painterResource(id = R.drawable.ic_scan),
        contentDescription = null,
        tint = Color.White
      )
    }, {
      viewModel.getPredictionDisease(plant, context)
    })
    // Hide FAB when composable cleared
    onDispose {
      setFabBehavior(false, {}, {})
    }

  }


  Surface(
    modifier = Modifier.fillMaxSize()
  ) {
    LazyColumn() {
      item() {
        val noCamAccessMessage = stringResource(id = R.string.no_camera_access)
        PlantImageComp(
          currentImage = currentImageState,
          onGalleryClick = {
            imagePickerLauncher.launch("image/*")
          },
          onCameraClick = {
            val permissionCheckResult =
              ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
              cameraLauncher.launch(tempFileUri)
            } else {
              appState.coroutineScope.launch {
                appState.showSnackbar(message = noCamAccessMessage)
              }
            }
          },
          onClear = {
            viewModel.predictedDiseaseState.value = UIState.Default
            viewModel.currentImage.value = null
          })
      }
      stickyHeader {
        PlantTitle("Deteksi penyakit pada ${plant.title}", plant.botanical_name)
      }
      item {
        val showPredicted = predictedDiseaseState.value is UIState.Success<PlantDisease>
        val showList =
          predictedDiseaseState.value is UIState.Default && diseaseListState.value is UIState.Success<List<PlantDisease>>
        if (showPredicted) {
          PredictedDetailComp(predictedDiseaseState = predictedDiseaseState)
        } else if (showList) {
          DiseaseListComp(diseaseListState = diseaseListState)
        } else {
          val stateToShow =
            if (predictedDiseaseState.value is UIState.Default) diseaseListState.value else predictedDiseaseState.value
          StatusComp(state = stateToShow)
        }
      }
    }

  }
}

@Composable
fun PlantImageComp(
  currentImage: State<Uri?>,
  onGalleryClick: () -> Unit,
  onCameraClick: () -> Unit,
  onClear: () -> Unit,
) {
  Surface(tonalElevation = 4.dp) {
    Box(
      modifier = Modifier
        .height(400.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
        .background(Gray200)
    ) {
      if (currentImage.value != null) {
        AsyncImage(
          model = currentImage.value, contentDescription = "Citra tumbuhan",
          modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
        )
        Box(
          modifier = Modifier
            .padding(16.dp)
            .align(Alignment.BottomEnd)
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.Red)
            .padding(8.dp)
            .clickable {
              onClear()
            }
        ) {
          Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            tint = Color.White
          )
        }
      } else {

        Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            "Pilih atau ambil citra.",
            style = MaterialTheme.typography.titleSmall,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          )
          {
            Surface(
              shadowElevation = 4.dp,
              tonalElevation = 4.dp,
              shape = CircleShape,
              modifier = Modifier
                .clip(CircleShape)
                .clickable { onGalleryClick() },
            ) {
              Image(
                modifier = Modifier
                  .padding(8.dp)
                  .size(36.dp),
                painter = painterResource(id = R.drawable.image_placeholder),
                contentDescription = "Gallery",
              )
            }
            Surface(
              shadowElevation = 4.dp,
              tonalElevation = 4.dp,
              shape = CircleShape,
              modifier = Modifier
                .clip(CircleShape)
                .clickable {
                  onCameraClick()
                },
            ) {
              Image(
                modifier = Modifier
                  .padding(8.dp)
                  .size(36.dp),
                painter = painterResource(id = R.drawable.ic_cam),
                contentDescription = "Gallery",
              )
            }
          }


        }
      }
//
    }
  }
}


@Composable
fun PredictedDetailComp(predictedDiseaseState: State<UIState<PlantDisease>>) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 16.dp)
  ) {
    val disease: PlantDisease =
      (predictedDiseaseState.value as UIState.Success<PlantDisease>).data!!
    if (disease.name == HEALTHY_NAME) {
      Text(
        "Tumbuhan ${disease.title_id}",
        style = MaterialTheme.typography.titleMedium,
      )
      Text(
        "Tidak terdeteksi penyakit apapun pada tumbuhan",
        style = MaterialTheme.typography.bodyMedium,
      )
    } else {
      Text(
        "Penyakit terdeteksi",
        style = MaterialTheme.typography.titleMedium,
      )
      Text(
        disease.title + " / " + disease.title_id,
        style = MaterialTheme.typography.bodyMedium,
      )
      SimpleExpandable(title = "Penyebab", description = disease.cause, true)
      SimpleExpandable(title = "Pengendalian", description = disease.treatment)
      SimpleExpandable(title = "Pengobatan", description = disease.medicine)
    }

  }
}

@Composable
fun DiseaseListComp(diseaseListState: State<UIState<List<PlantDisease>>>) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      "Jenis penyakit yang dikenali",
      style = MaterialTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(4.dp))
    (diseaseListState.value as UIState.Success<List<PlantDisease>>).data?.mapIndexed { index, plantDisease ->
      Text(
        "${index + 1}. ${plantDisease.title} (${plantDisease.title_id})",
        style = MaterialTheme.typography.bodyLarge,
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    val viewModel: GlobalViewModel = viewModel()
    val func: (Boolean, @Composable () -> Unit, () -> Unit) -> Unit = { _, _, _ -> }
    PlantDiseaseDetectionScreen(rememberAppState(), viewModel, func)
  }
}