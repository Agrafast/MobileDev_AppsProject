package com.agrafast.ui.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.rememberAppState
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.theme.AgraFastTheme
import com.agrafast.ui.theme.Poppins
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import okhttp3.internal.wait

@Composable
fun SplashScreen(
  appState: AppState
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.onTertiaryContainer)
  ) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.logo_splash))
    val logoAnimationState =
      animateLottieCompositionAsState(composition = composition)
    Column(
      Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
        .padding(bottom = 22.dp),
      verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        color = Color.White,
        text = "AGRAFAST",
        fontFamily = Poppins,
        style = MaterialTheme.typography.titleLarge,
        fontSize = 28.sp
      )
      Text(
        modifier = Modifier.offset(y = (-12).dp),
        color = Color.White,
        text = "DISCOVER, DIAGNOSE, SUCCEED",
        style = MaterialTheme.typography.titleLarge,
        fontSize = 16.sp
      )
    }
    LottieAnimation(
      composition = composition,
      progress = logoAnimationState.progress
    )
    if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
      appState.navController.navigate(Screen.Home.route){
        popUpTo(Screen.Splash.route){
          inclusive = true
        }
      }
    }
  }
}

@Preview()
@Composable
fun DefaultPreview(){
  AgraFastTheme {
    SplashScreen(appState = rememberAppState())
  }
}