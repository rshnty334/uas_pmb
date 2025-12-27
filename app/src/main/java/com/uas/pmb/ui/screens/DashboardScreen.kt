package com.uas.pmb.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: PmbViewModel = hiltViewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val user by viewModel.profile.collectAsState()
    val mabaList by viewModel.mabaList.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        viewModel.loadMabas()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu PMB STIS", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Profil Saya") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("profile")
                    },
                    icon = { Icon(Icons.Default.Person, null) }
                )
                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        viewModel.logout { navController.navigate("login") { popUpTo(0) } }
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, null) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                StisTopAppBar(
                    title = "Dashboard PMB",
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { padding ->
            val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.refreshData() })

            // PERBAIKAN: Masukkan seluruh logika ke dalam Box dengan pullRefresh
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState) // Harus ada di sini agar bisa ditarik
            ) {
                if (user == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1B3E68))
                        Spacer(Modifier.height(16.dp))
                        Text("Memuat data profil...")

                        // Tombol darurat jika loading terlalu lama
                        TextButton(onClick = { viewModel.loadProfile() }) {
                            Text("Coba Lagi")
                        }
                    }
                } else {
                    // Pengecekan role setelah data user tersedia
                    Column {
                        if (user?.role == "ROLE_ADMIN") {
                            AdminView(mabaList, viewModel)
                        } else {
                            val myData = mabaList.find { it.emailMaba == user?.email }
                            val stats = Triple(
                                mabaList.size,
                                mabaList.count { it.statusVerifikasi == "Terverifikasi" },
                                mabaList.count { it.statusKelulusan == "Lulus" }
                            )
                            UserDashboardContent(myData, stats, navController)
                        }
                    }
                }

                // Indikator muncul di atas konten saat refreshing
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
fun StatusCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1B3E68) // Default Biru STIS
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StisTopAppBar(title: String, onMenuClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF1B3E68) // Biru Navy Resmi
        )
    )
}