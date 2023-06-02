package com.agrafast.ui.navigation

sealed class Screen(val route: String) {
  object Home : Screen("home")
  object PlantList : Screen("plant")
  object PlantDetail : Screen("plant/{plantId}") {
    fun to(plantId: Long) = "home/${plantId}"
  }

  object DiseaseDetector : Screen("detect/{plant}")
  object UserPlant : Screen("userplant")
  object Profil : Screen("profil")

  //Authentication
  object Login : Screen("auth/login")
  object Register : Screen("auth/register")
  object ResetPassword : Screen("auth/reset")
  object ForgotPassword : Screen("auth/forgot")
}
