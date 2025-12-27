package com.uas.pmb.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uas.pmb.data.MabaDto

@Composable
fun MabaDetailView(maba: MabaDto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Status Pendaftaran", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                val isVerified = maba.statusVerifikasi == "Terverifikasi"
                StatusBadge(
                    text = if (isVerified) "Terverifikasi" else "Belum Terverifikasi",
                    color = if (isVerified) Color(0xFF4CAF50) else Color.Gray
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            DetailItem("Nama Lengkap", maba.namaMaba)
            DetailItem("NIK", maba.nik)
            DetailItem("Email", maba.emailMaba)
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}