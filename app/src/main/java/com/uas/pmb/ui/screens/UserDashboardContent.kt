package com.uas.pmb.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uas.pmb.data.MabaDto

@Composable
fun UserDashboardContent(
    myData: MabaDto?,
    stats: Triple<Int, Int, Int>,
    navController: NavController
) {
    val (total, verified, graduated) = stats

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Statistik Pendaftaran Global", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusCard("Pendaftar", "$total", Modifier.weight(1f))
            StatusCard("Verified", "$verified", Modifier.weight(1f), Color(0xFF4CAF50))
            StatusCard("Lulus", "$graduated", Modifier.weight(1f), Color(0xFF2196F3))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text("Status Pendaftaran Anda", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        if (myData == null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Anda belum melengkapi data pendaftaran.")
                    Button(onClick = { navController.navigate("add_maba") }, modifier = Modifier.fillMaxWidth()) {
                        Text("Daftar Sekarang")
                    }
                }
            }
        } else {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(text = myData.namaMaba, style = MaterialTheme.typography.headlineSmall)
                    // PENTING: Gunakan 'nik' kecil agar tidak NULL
                    Text(text = "NIK: ${myData.nik}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Verifikasi", style = MaterialTheme.typography.labelSmall)
                            Text(myData.statusVerifikasi, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Kelulusan", style = MaterialTheme.typography.labelSmall)
                            Text(myData.statusKelulusan, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { navController.navigate("edit_maba") }, // Navigasi ke rute edit
                        modifier = Modifier.fillMaxWidth(),
                        // Terkunci jika status sudah SUDAH
                        enabled = myData.statusVerifikasi != "Terverifikasi",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (myData.statusVerifikasi == "Terverifikasi") Color.Gray else Color(0xFF1B3E68)
                        )
                    ) {
                        Text(if (myData.statusVerifikasi == "Terverifikasi") "Data Terkunci" else "Edit Data")
                    }
                }
            }
        }
    }
}