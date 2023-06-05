package com.agrafast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.agrafast.ui.navigation.NavItem
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.screen.detail.PlantDetailScreen
import com.agrafast.ui.screen.detection.PlantDiseaseDetectionScreen
import com.agrafast.ui.screen.home.HomeScreen
import com.agrafast.ui.screen.plant.PlantListScreen
import com.agrafast.ui.screen.profil.ProfileScreen
import com.agrafast.ui.screen.usersplant.UserPlantListScreen
import com.agrafast.ui.theme.AgraFastTheme

@Composable
fun AgraFastApp(
  navController: NavHostController = rememberNavController()
) {
  val navItems = listOf(
    NavItem(stringResource(R.string.home), Icons.Default.Home, Screen.Home),
    NavItem(
      stringResource(R.string.user_plant),
      ImageVector.vectorResource(id = R.drawable.ic_plant),
      Screen.UserPlantList,
    ),
    NavItem(stringResource(R.string.profile), Icons.Default.Person, Screen.Profil),
  )

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  // FAB Stuff
  val showFab = remember { mutableStateOf(false) }
  val fabOnclick = remember { mutableStateOf<(() -> Unit)?>(null) }
  val fabContent = remember { mutableStateOf<@Composable () -> Unit>({}) }
  val setFabBehavior : (Boolean, @Composable () -> Unit, () -> Unit) -> Unit  = {show, content, onClick ->
    showFab.value = show
    fabContent.value = content
    fabOnclick.value = onClick
  }

  Scaffold(
    bottomBar = {
      val isVisible = navItems.map { it.screen.route }.contains(currentRoute)

      BottomBarComponent(navItems, isVisible, currentRoute) {
        navController.navigate(it)
      }
    },
    floatingActionButton = {
      if (showFab.value) {
        FloatingActionButton(onClick = { fabOnclick.value?.invoke() }) {
          fabContent.value.invoke()
        }
      }
    }
  ) { innerPadding ->
    val viewModel: GlobalViewModel = hiltViewModel()
    NavHost(
      navController = navController,
      startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)
    ) {
      composable(Screen.Home.route) {
        HomeScreen(navController, viewModel)
      }
      composable(Screen.PlantList.route) {
        PlantListScreen(navController, viewModel)
      }
      composable(Screen.UserPlantList.route) {
        UserPlantListScreen(navController, viewModel)
      }
      composable(Screen.Profil.route) {
        ProfileScreen()
      }
      composable(route = Screen.PlantDetail.route) {
        PlantDetailScreen(navController, viewModel)
      }
      composable(Screen.PlantDiseaseDetection.route) {
        PlantDiseaseDetectionScreen(
          viewModel,
          setFabBehavior = setFabBehavior,
        )
      }
    }
  }
}

@Composable
fun BottomBarComponent(
  navItems: List<NavItem>,
  isVisible: Boolean,
  currentRoute: String?,
  onClickNavItem: (String) -> Unit
) {
  AnimatedVisibility(
    visible = isVisible,
    enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
    exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
  ) {
    NavigationBar {
      navItems.forEach {
        NavigationBarItem(
          selected = currentRoute == it.screen.route,
          onClick = { onClickNavItem(it.screen.route) },
          label = {
            Text(text = it.title, fontWeight = FontWeight.SemiBold)
          },
          icon = {
            Icon(it.icon, contentDescription = "${it.title}_page")
          }
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    AgraFastApp()
  }
}