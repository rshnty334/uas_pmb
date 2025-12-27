package com.uas.pmb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uas.pmb.ui.theme.StisBluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: PmbViewModel = hiltViewModel()) {
    val user by viewModel.profile.collectAsState()
    val mabaList by viewModel.mabaList.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        viewModel.loadMabas()
    }

    val myMaba = mabaList.find { it.emailMaba == user?.email }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Saya") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StisBluePrimary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (user?.role == "ROLE_USER") {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                        Text("Pendaftaran", Modifier.padding(16.dp))
                    }
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                        Text("Akun", Modifier.padding(16.dp))
                    }
                }
            }

            Box(modifier = Modifier.padding(16.dp)) {
                when {
                    user?.role == "ROLE_ADMIN" -> AccountView(user, viewModel, navController)
                    selectedTab == 0 -> {
                        if (myMaba != null) MabaDetailView(myMaba)
                        else EmptyMabaView(onRefresh = { viewModel.loadMabas() })
                    }
                    selectedTab == 1 -> AccountView(user, viewModel, navController)
                }
            }
        }
    }
}