package com.agrafast.ui.navigation

sealed class Screen(val route: String) {
  object Splash : Screen("splash")
  object OnBoarding : Screen("onboarding")
  object Home : Screen("home")
  object PlantList : Screen("plant-list/{level}") {
    fun to(level: Int) = "plant-list/${level}"
  }


  object PlantDetail : Screen("plant/{plantId}") {
    fun to(plantId: Long) = "home/${plantId}"
  }

  object PlantDiseaseDetection : Screen("detect/{plant}")
  object UserPlantList : Screen("userplant")
  object Profil : Screen("profil")
  object UpdateProfile : Screen("profil/update/{type}") {
    fun to(updateType: String) = "profil/update/${updateType}"
  }


  //Authentication
  object Login : Screen("auth/login")
  object Register : Screen("auth/register")
  object ResetPassword : Screen("auth/reset")
  object ForgotPassword : Screen("auth/forgot")
}
