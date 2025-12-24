package com.uas.pmb.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun PmbNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController) // Memanggil dari LoginScreen.kt
        }
        composable("dashboard") {
            DashboardScreen(navController) // Memanggil dari DashboardScreen.kt
        }
        composable("profile") {
            ProfileScreen(navController) // Memanggil dari ProfileScreen.kt
        }
        composable("add_maba") {
            AddMabaScreen(navController) // Memanggil dari AddMabaScreen.kt
        }
    }
}