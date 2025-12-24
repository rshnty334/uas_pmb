package com.uas.pmb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourpackage.ui.PmbViewModel

@Composable
fun DashboardScreen(navController: NavController, viewModel: PmbViewModel = hiltViewModel()) {
    // Memicu pengambilan data saat layar pertama kali dibuka
    LaunchedEffect(Unit) {
        viewModel.loadMabas()
    }

    val mabaList by viewModel.mabaList.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_maba") }) {
                // Menggunakan Icons.Default.Add
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text("Ke Profil Pengguna")
                }
            }
        }
    ) { padding ->
        if (mabaList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada data mahasiswa.")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                // Sekarang 'items' akan mengenali mabaList sebagai List<MabaDto>
                items(mabaList) { maba ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Properti maba sekarang akan terbaca (namaMaba & NIK)
                            Text(text = maba.namaMaba, style = MaterialTheme.typography.titleLarge)
                            Text(text = "NIK: ${maba.NIK}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}