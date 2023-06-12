package com.agrafast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.agrafast.ui.navigation.NavItem
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.AuthViewModel
import com.agrafast.ui.screen.GlobalViewModel
import com.agrafast.ui.screen.authetication.ResetPasswordScreen
import com.agrafast.ui.screen.authetication.login.LoginScreen
import com.agrafast.ui.screen.authetication.register.RegisterScreen
import com.agrafast.ui.screen.detail.PlantDetailScreen
import com.agrafast.ui.screen.detection.PlantDiseaseDetectionScreen
import com.agrafast.ui.screen.home.HomeScreen
import com.agrafast.ui.screen.plant.PlantListScreen
import com.agrafast.ui.screen.profil.ProfileScreen
import com.agrafast.ui.screen.profil.UpdateProfileScreen
import com.agrafast.ui.screen.profil.UpdateType
import com.agrafast.ui.screen.splash.SplashScreen
import com.agrafast.ui.screen.usersplant.UserPlantListScreen
import com.agrafast.ui.theme.AgraFastTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AgraFastApp(
  appState: AppState = rememberAppState(),
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

  val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  // FAB Stuff
  val showFab = remember { mutableStateOf(false) }
  val fabOnclick = remember { mutableStateOf<(() -> Unit)?>(null) }
  val fabContent = remember { mutableStateOf<@Composable () -> Unit>({}) }
  val setFabBehavior: (Boolean, @Composable () -> Unit, () -> Unit) -> Unit =
    { show, content, onClick ->
      showFab.value = show
      fabContent.value = content
      fabOnclick.value = onClick
    }

  Scaffold(
    snackbarHost = { SnackbarHost(hostState = appState.snackbarHostState) },
    bottomBar = {
      val isVisible = navItems.map { it.screen.route }.contains(currentRoute)

      BottomBarComponent(navItems, isVisible, currentRoute) {
        appState.navController.apply {
          popBackStack()
          navigate(it)
        }
      }
    },
    floatingActionButton = {
      AnimatedVisibility(
        visible = showFab.value,
        enter = fadeIn(),
        exit = fadeOut(),
      ) {
        FloatingActionButton(onClick = { fabOnclick.value?.invoke() }) {
          fabContent.value.invoke()
        }
      }
    }
  ) { innerPadding ->
    val viewModel: GlobalViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    NavHost(
      navController = appState.navController,
      startDestination = Screen.Splash.route, modifier = Modifier.padding(innerPadding)
    ) {
      composable(route = Screen.Splash.route) {
        SplashScreen(appState = appState, authViewModel = authViewModel)
      }
      composable(Screen.Login.route) {
        LoginScreen(appState = appState, authViewModel = authViewModel)
      }
      composable(Screen.Register.route) {
        RegisterScreen(appState = appState, authViewModel = authViewModel)
      }
      composable(Screen.Register.route) {
        RegisterScreen(appState = appState, authViewModel = authViewModel)
      }
      composable(Screen.ResetPassword.route) {
        ResetPasswordScreen(appState = appState, authViewModel = authViewModel)
      }
      composable(route = Screen.Home.route) {
        HomeScreen(appState = appState, sharedViewModel = viewModel, authViewModel = authViewModel)
      }
      composable(route = Screen.PlantList.route) {
        PlantListScreen(appState = appState, sharedViewModel = viewModel)
      }
      composable(route = Screen.UserPlantList.route) {
        UserPlantListScreen(
          appState = appState,
          sharedViewModel = viewModel,
          authViewModel = authViewModel
        )
      }
      composable(route = Screen.Profil.route) {
        ProfileScreen(appState = appState, authViewModel = authViewModel)
      }
      composable(
        route = Screen.UpdateProfile.route,
        arguments = listOf(navArgument("type") { type = NavType.StringType })
      ) {
        val type = when (it.arguments?.getString("type")) {
          "email" -> UpdateType.EMAIL
          "photo" -> UpdateType.PHOTO
          else -> UpdateType.PROFILE
        }
        UpdateProfileScreen(appState = appState, authViewModel = authViewModel, type)
      }
      composable(route = Screen.PlantDetail.route) {
        PlantDetailScreen(appState = appState, sharedViewModel = viewModel, authViewModel)
      }
      composable(route = Screen.PlantDiseaseDetection.route) {
        PlantDiseaseDetectionScreen(
          appState = appState,
          sharedViewModel = viewModel,
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

class AppState(
  val snackbarHostState: SnackbarHostState,
  val navController: NavHostController,
  val coroutineScope: CoroutineScope,
) {
  fun showSnackbar(
    message: String, actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onResult: (SnackbarResult) -> Unit = {}
  ) {
    coroutineScope.launch {
      val snackbarResult = snackbarHostState.showSnackbar(
        actionLabel = actionLabel,
        message = message,
        duration = duration
      )
      onResult(snackbarResult)
    }
  }
}

@Composable
fun rememberAppState(
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  navController: NavHostController = rememberNavController(),
  coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(snackbarHostState, navController, coroutineScope) {
  AppState(
    snackbarHostState = snackbarHostState,
    navController = navController,
    coroutineScope = coroutineScope,
  )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    AgraFastApp()
  }
}