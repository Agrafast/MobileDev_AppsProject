package com.agrafast.ui.screen.detection

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.agrafast.R
import com.agrafast.domain.UIState
import com.agrafast.domain.model.Plant
import com.agrafast.domain.model.PlantDisease
import com.agrafast.ui.component.SimpleExpandable
import com.agrafast.ui.component.StatusComp
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.Gray200
import com.agrafast.ui.theme.Gray600
import com.agrafast.util.HEALTHY_NAME
import com.agrafast.util.Helper
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlantDiseaseDetectionScreen(
  sharedViewModel: GlobalViewModel,
  setFabBehavior: (Boolean, @Composable () -> Unit, () -> Unit) -> Unit
) {
  val context = LocalContext.current
  val viewModel: PlantDiseaseDetectionViewModel = hiltViewModel()
  viewModel.currentPlant = sharedViewModel.detectionPlant!!
  val imagePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
    onResult = {
      viewModel.setCurrentImage(Helper.createFileFromUri(it!!, context))
    })

  // State
  val diseaseListState = viewModel.plantDiseaseListState.collectAsState()
  val predictedDiseaseState = viewModel.predictedDiseaseState.collectAsState()
  val currentImageState = viewModel.currentImage.collectAsState()

  // SideEffects
  // Launch one time
  LaunchedEffect(Unit) {
    viewModel.getPlantDiseases()
  }
  LaunchedEffect(currentImageState.value) {
    val showFab = currentImageState.value != null
    setFabBehavior(showFab, {
      Icon(
        modifier = Modifier.size(48.dp),
        painter = painterResource(id = R.drawable.ic_scan),
        contentDescription = null,
        tint = Color.White
      )
    }, {
      viewModel.getPredictionDisease()
    })
  }


  Surface(
    modifier = Modifier.fillMaxSize()
  ) {
    LazyColumn() {
      item() {
        PlantImageComp(imagePickerLauncher, currentImageState, onClear = {
          viewModel.predictedDiseaseState.value = UIState.Default
          viewModel.currentImage.value = null
        })
      }
      stickyHeader {
          PlantDetailComp(viewModel.currentPlant)
      }
      item {
        val showPredicted = predictedDiseaseState.value is UIState.Success<PlantDisease>
        val showList = predictedDiseaseState.value !is UIState.Loading && diseaseListState.value is UIState.Success<List<PlantDisease>>
        if(showPredicted){
          PredictedDetailComp(predictedDiseaseState = predictedDiseaseState)
        } else if(showList){
          DiseaseListComp(diseaseListState = diseaseListState )
        }
        else {
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
  imagePickerLauncher: ActivityResultLauncher<String>,
  currentImage: State<File?>,
  onClear: () -> Unit
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
          model = currentImage.value!!.toUri(), contentDescription = "Citra tumbuhan",
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
          Image(
            modifier = Modifier
              .alpha(0.5f)
              .clickable {
                imagePickerLauncher.launch("image/*")
              },
            painter = painterResource(id = R.drawable.image_placeholder),
            contentDescription = "Citra tumbuhan",
          )
          Text(
            "Pilih atau ambil citra.",
            style = MaterialTheme.typography.labelLarge,
            color = Gray600
          )
        }
      }
//
    }
  }
}

@Composable
fun PlantDetailComp(plant: Plant) {
  Surface(
    color = MaterialTheme.colorScheme.background,
    tonalElevation = 4.dp
  ) {
    Row(
      modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 4.dp)
        .height(64.dp)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(verticalArrangement = Arrangement.Center) {
        Text(
          text = "Deteksi penyakit pada ${plant.title}",
          style = MaterialTheme.typography.titleLarge,
        )
        Text(
          text = plant.titleLatin,
          style = MaterialTheme.typography.bodyLarge,
        )
      }
    }
  }
}

@Composable
fun PredictedDetailComp(predictedDiseaseState: State<UIState<PlantDisease>>){
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 16.dp)
  ) {
    val disease : PlantDisease = (predictedDiseaseState.value as UIState.Success<PlantDisease>).data!!
    if(disease.name == HEALTHY_NAME){
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
      Spacer(modifier = Modifier.height(8.dp))
      SimpleExpandable(title = "Penyebab" , description = disease.cause, true)
      Spacer(modifier = Modifier.height(8.dp))
      SimpleExpandable(title = "Pengendalian" , description = disease.treatment )
      Spacer(modifier = Modifier.height(8.dp))
      SimpleExpandable(title = "Pengobatan" , description = disease.medicine)
    }

  }
}

@Composable
fun DiseaseListComp(diseaseListState: State<UIState<List<PlantDisease>>>){
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

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//  AgraFastTheme {
//    val viewModel: GlobalViewModel = viewModel()
//    PlantDiseaseDetectionScreen(viewModel, {}, {})
//  }
//}