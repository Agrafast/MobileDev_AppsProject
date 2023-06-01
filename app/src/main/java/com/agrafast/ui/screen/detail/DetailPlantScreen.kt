package com.agrafast.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agrafast.domain.model.TutorialPlant
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.theme.AgraFastTheme

@Composable
fun PlantDetailScreen(
  sharedViewModel: GlobalViewModel,
) {
  val plant: TutorialPlant = sharedViewModel.tutorialPlant!!
  val loremIpsum = LoremIpsum(20)
  Surface {
    Column(
      modifier = Modifier
        .padding(bottom = 16.dp)
        .fillMaxSize()
        .verticalScroll(
          rememberScrollState()
        )
    ) {
      // TODO -> Change to Network Image (AsyncImage)
      Image(

        painter = plant.image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .height(320.dp)
          .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
      )
      Spacer(modifier = Modifier.height(12.dp))
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = plant.title,
        style = MaterialTheme.typography.titleLarge,
      )
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = "Nama latin ni boss",
        style = MaterialTheme.typography.bodyLarge,
      ) // TODO Update based API
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = loremIpsum.values.joinToString("") ,
        style = MaterialTheme.typography.bodyMedium,
      ) // TODO Update based API
      Spacer(modifier = Modifier.height(16.dp))
      Divider()

      PlantingTutorial()
    }
  }
}


@Composable
fun PlantingTutorial(){
  Column( modifier = Modifier.padding(horizontal = 16.dp),) {
    Text(
      modifier = Modifier.padding(horizontal = 16.dp),
      text = "Nama latin ni boss",
      style = MaterialTheme.typography.bodyLarge,
    )
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    val viewModel: GlobalViewModel = viewModel()
    PlantDetailScreen(viewModel)
  }
}