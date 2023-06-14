package com.agrafast.ui.screen.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.ui.component.PrimaryButton
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.theme.Gray200

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
  appState: AppState,
  splashViewModel: SplashViewModel = hiltViewModel()
) {
  val pages = remember { OnBoardingItem.data }
  val pagerState = rememberPagerState { 3 }
  Surface(modifier = Modifier.fillMaxSize()) {
    Box() {
      Column(
        Modifier
          .fillMaxSize()
          .align(Alignment.Center),
        verticalArrangement = Arrangement.Center
      ) {
        HorizontalPager(
          verticalAlignment = Alignment.Top,
          state = pagerState,
        ) { position ->
          OnBoardingPage(pages[position])
        }
        Spacer(modifier = Modifier.height(32.dp))
        Indicators(
          size = pagerState.pageCount,
          index = pagerState.currentPage
        )
      }
      AnimatedVisibility(
        visible = pagerState.currentPage + 1 == pagerState.pageCount,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
          .align(Alignment.BottomCenter)
      ) {
        Row(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 24.dp)
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Box(modifier = Modifier.weight(1f)) {
            PrimaryButton(
              color = MaterialTheme.colorScheme.onBackground,
              background = MaterialTheme.colorScheme.background,
              text = "Masuk",
              onClick = {
                splashViewModel.disableNextOnboarding()
                appState.navController.navigate(Screen.Login.route) {
                  popUpTo(Screen.OnBoarding.route) {
                    inclusive = true
                  }
                }
              })
          }
          Box(modifier = Modifier.weight(1f)) {
            PrimaryButton(text = "Daftar", onClick = {
              splashViewModel.disableNextOnboarding()
              appState.navController.navigate(Screen.Register.route) {
                popUpTo(Screen.OnBoarding.route) {
                  inclusive = true
                }
              }
            })
          }
        }
      }
    }
  }
}

@Composable
fun OnBoardingPage(item: OnBoardingItem) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
  ) {
    Image(
      painter = painterResource(id = item.image),
      contentDescription = "selamat datang",
      modifier = Modifier
        .height(300.dp)
        .padding(horizontal = 36.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      textAlign = TextAlign.Center,
      text = item.title,
      style = MaterialTheme.typography.displaySmall,
      letterSpacing = 1.sp,
    )
    Text(
      textAlign = TextAlign.Center,
      text = item.desc,
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}


@Composable
fun Indicators(modifier: Modifier = Modifier, size: Int, index: Int) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
  ) {
    repeat(size) {
      Indicator(isSelected = it == index)
    }
  }
}

@Composable
fun Indicator(isSelected: Boolean) {
  val width = animateDpAsState(
    targetValue = if (isSelected) 36.dp else 12.dp,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
  )
  Box(
    modifier = Modifier
      .height(12.dp)
      .width(width.value)
      .clip(CircleShape)
      .background(
        color = if (isSelected) MaterialTheme.colorScheme.primary else Gray200
      )
  )
}


class OnBoardingItem(
  val image: Int,
  val title: String,
  val desc: String
) {
  companion object {
    val data: List<OnBoardingItem> = listOf(
      OnBoardingItem(
        R.drawable.ob_find_plant,
        "Cari Tanamanmu",
        "Cari tanaman yang cocok ditanam di daerahmu dan raih kesuksesan dalam bisnis agrikultur"
      ),
      OnBoardingItem(
        R.drawable.ob_planting_tutorial,
        "Tutorial Menanam",
        "Temukan kaidah-kaidah menanam yang telah teruji dan diterapkan secara luas"
      ),
      OnBoardingItem(
        R.drawable.ob_detect_disease,
        "Identifikasi Penyakit",
        "Identifikasi penyakit yang ada pada tanamanmu dan temukan solusinya secara cepat dan tepat"
      )
    )

  }
}