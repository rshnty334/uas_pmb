package com.uas.pmb.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uas.pmb.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, viewModel: PmbViewModel = hiltViewModel()) {
    val scale = remember { Animatable(0f) }
    val isLoggedIn by viewModel.isUserLoggedIn.collectAsState()

    LaunchedEffect(Unit) {
        // Jalankan pengecekan token dan animasi secara paralel
        viewModel.checkLoginStatus()

        scale.animateTo(
            targetValue = 0.8f,
            animationSpec = tween(durationMillis = 1000)
        )

        // Tunggu sebentar agar logo terlihat
        delay(1000)

        // Navigasi berdasarkan status login
        if (isLoggedIn == true) {
            navController.navigate("dashboard") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B3E68)), // Biru Navy STIS
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_stis),
            contentDescription = "Logo STIS",
            modifier = Modifier
                .size(180.dp)
                .scale(scale.value)
        )
    }
}