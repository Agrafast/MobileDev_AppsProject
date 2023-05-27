package com.agrafast.ui.navigation

sealed class Screen(val route: String) {
  object Home : Screen("home")
  object Detail : Screen("home/{plantId}") {
    fun to(plantId: Long) = "home/${plantId}"
  }
}
