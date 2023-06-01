package com.agrafast.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.agrafast.R
import com.agrafast.domain.model.DiseasePlant
import com.agrafast.domain.model.TutorialPlant
import com.agrafast.ui.theme.AgraFastTheme


@Composable
fun HomeScreen() {
//  Surface(
//  ) {
  LazyColumn(
    contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
  ) {
    item { UserInfo() }
    item { SectionTitle(text = stringResource(id = R.string.disease_detector_title), onClick = {}) }
    item { DiseaseDetectionComp() }
    item { SectionTitle(text = stringResource(id = R.string.plant_stuff_title), onClick = {}) }
    item { PlantStuffComp() }
  }
//  }
}

@Composable
fun UserInfo() {
  Row(
    modifier = Modifier
      .padding(horizontal = 16.dp)
  ) {
    Box(
      modifier = Modifier
        .background(color = MaterialTheme.colorScheme.background, CircleShape)
        .size(64.dp)
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
        text = "Indi nih Boss",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.fillMaxWidth()
      )
      Text(
        text = "Senggol dongg!!!!!",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }

}

@Composable
fun SectionTitle(text: String, onClick: () -> Unit) {
  Row(
    modifier = Modifier
      .padding(horizontal = 16.dp)
      .padding(top = 16.dp, bottom = 4.dp)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.titleLarge,
    )
  }
}

@Composable
fun DiseaseDetectionComp() {

  val _plants = listOf(
    DiseasePlant("potato", "Kentang", painterResource(id = R.drawable.potato_banner)),
    DiseasePlant("maize", "Jagung", painterResource(id = R.drawable.maize_banner)),
    DiseasePlant("rice", "Padi", painterResource(id = R.drawable.rice_banner)),
  )
  val plants = remember { _plants.shuffled() }

  Column(
    Modifier.padding(horizontal = 16.dp)
  ) {
    DiseaseDetectionPlantCard(plant = plants[0], height = 192.dp)
    Spacer(modifier = Modifier.height(16.dp))
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier.fillMaxWidth()
        .height(112.dp))
    {
      items(plants.subList(1, plants.size), ) {
        DiseaseDetectionPlantCard(plant = it, height = 112.dp)
      }
    }
  }
}

@Composable
fun DiseaseDetectionPlantCard(plant: DiseasePlant, height: Dp) {
  Box(
    modifier = Modifier
      .height(height = height)
      .clip(RoundedCornerShape(12.dp))
      .fillMaxWidth()
  ) {
    Image(
      painter = plant.image,
      contentDescription = plant.title,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxSize()
        .fillMaxHeight()
    )
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          brush = Brush.verticalGradient(
            listOf(
              Color.Black, Color.Transparent
            ),
            startY = Float.POSITIVE_INFINITY,
            endY = height.value - 64f
          )
        )
    ) {
      Text(
        modifier = Modifier
          .padding(horizontal = 8.dp, vertical = 4.dp)
          .align(
            Alignment
              .BottomStart
          ),
        text = plant.title,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White
      )
    }
  }
}


@Composable
fun PlantStuffComp() {
  val plants = listOf(
    TutorialPlant("potato", "Kentang", painterResource(id = R.drawable.potato_banner)),
    TutorialPlant("maize", "Jagung", painterResource(id = R.drawable.maize_banner)),
    TutorialPlant("rice", "Padi", painterResource(id = R.drawable.rice_banner)),
    TutorialPlant("potato", "Kentang", painterResource(id = R.drawable.potato_banner)),
    TutorialPlant("maize", "Jagung", painterResource(id = R.drawable.maize_banner)),
    TutorialPlant("rice", "Padi", painterResource(id = R.drawable.rice_banner)),
    TutorialPlant("potato", "Kentang", painterResource(id = R.drawable.potato_banner)),
    TutorialPlant("maize", "Jagung", painterResource(id = R.drawable.maize_banner)),
    TutorialPlant("rice", "Padi", painterResource(id = R.drawable.rice_banner)),
  )

  val halfNumber: Int = plants.size / 2
  val plantsA = plants.subList(0, halfNumber)
  val plantsB = plants.subList(halfNumber, plants.size)
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(horizontal = 16.dp)
  ) {
    items(plantsA) {
      PlantCard(plant = it)
    }
  }
  Spacer(modifier = Modifier.height(8.dp))
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(horizontal = 16.dp)
  ) {
    items(plantsB) {
      PlantCard(plant = it)
    }
  }
}

@Composable
fun PlantCard(plant: TutorialPlant) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Image(
      painter = plant.image,
      contentDescription = plant.title,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .size(160.dp)
        .clip(RoundedCornerShape(16.dp))
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = plant.title,
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    HomeScreen()
  }
}