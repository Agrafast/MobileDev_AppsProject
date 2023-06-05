package com.agrafast.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.agrafast.R
import com.agrafast.domain.UIState
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun <T> StatusComp(state: UIState<T>) {
  val text = when (state) {
    is UIState.Loading -> "Please wait."
    is UIState.Empty -> "Ups, looks like its empty."
    else -> "Sorry, something went wrong."
  }

  @DrawableRes
  val resId = if (state is UIState.Empty) R.drawable.ic_empty else R.drawable.ic_bug

  if (state is UIState.Empty || state is UIState.Loading || state is UIState.Error) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 100.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,

      ) {
      if (state is UIState.Loading) {
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_loading))
        val progress by animateLottieCompositionAsState(
          composition = composition,
          iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
          modifier = Modifier
            .height(192.dp)
            .fillMaxWidth(),
          composition = composition,
          progress = progress
        )
      } else {
        Image(
          modifier = Modifier
            .height(192.dp)
            .fillMaxWidth(),
          painter = painterResource(id = resId), contentDescription = null
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
  }
}