package com.agrafast.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agrafast.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun StatusComp(text: String = "Ups, nothing here." ) {
  val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_not_found))
  val progress by animateLottieCompositionAsState(composition = composition,
    iterations = LottieConstants.IterateForever
    )

  Column(
    modifier = Modifier.fillMaxSize().padding(bottom = 100.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,

  ) {
    LottieAnimation(
      modifier = Modifier
        .height(192.dp)
        .fillMaxWidth(),
      composition = composition,
      progress = progress
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = text, style = MaterialTheme.typography.labelLarge)
  }
}