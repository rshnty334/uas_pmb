package com.uas.pmb.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uas.pmb.data.MabaDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminView(mabaList: List<MabaDto>, viewModel: PmbViewModel) {
    var searchQuery by remember { mutableStateOf("") }

    // State untuk Filter Status (SEMUA, BELUM, SUDAH)
    val filterOptions = listOf("Semua", "Terverifikasi", "Lulus")
    var selectedFilter by remember { mutableStateOf("Semua") }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedMaba by remember { mutableStateOf<MabaDto?>(null) }

    // Logika Filtering Ganda: Search + Filter Status
    val filteredList = remember(searchQuery, selectedFilter, mabaList) {
        mabaList.filter { maba ->
            val matchesSearch = maba.namaMaba.contains(searchQuery, ignoreCase = true) ||
                    maba.nik.contains(searchQuery)
            val matchesStatus = if (selectedFilter == "Semua") true
            else maba.statusVerifikasi == selectedFilter
            matchesSearch && matchesStatus
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Cari Nama atau NIK") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, null) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Row Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterOptions.forEach { option ->
                FilterChip(
                    selected = selectedFilter == option,
                    onClick = { selectedFilter = option },
                    label = { Text(option) },
                    leadingIcon = if (selectedFilter == option) {
                        { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)) }
                    } else null
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // List Hasil Filter
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filteredList) { maba ->
                AdminMabaCard(maba = maba) {
                    selectedMaba = maba
                    showBottomSheet = true
                }
            }
        }
    }

    // Modal Bottom Sheet tetap sama
    if (showBottomSheet && selectedMaba != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            MabaDetailContent(
                maba = selectedMaba!!,
                onUpdateStatus = { updatedMaba ->
                    viewModel.updateMabaStatus(updatedMaba)
                    showBottomSheet = false
                }
            )
        }
    }
}

@Composable
fun AdminMabaCard(maba: MabaDto, onCardClick: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), onClick = onCardClick) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = maba.namaMaba, fontWeight = FontWeight.Bold)
                Text(text = "NIK: ${maba.nik}", style = MaterialTheme.typography.bodySmall) // nik kecil
            }
            StatusBadge(
                text = if (maba.statusVerifikasi == "Terverifikasi") "Terverifikasi" else "Belum Verifikasi",
                color = if (maba.statusVerifikasi == "Terverifikasi") Color.Green else Color.Gray
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Label Kelulusan (Hanya muncul jika sudah diverifikasi)
            if (maba.statusVerifikasi == "Terverifikasi") {
                StatusBadge(
                    text = maba.statusKelulusan, // LULUS, GUGUR, atau PENDING
                    color = when(maba.statusKelulusan) {
                        "Lulus" -> Color.Blue
                        "Gugur" -> Color.Red
                        else -> Color.Gray
                    }
                )
            }
        }
    }
}

@Composable
fun MabaDetailContent(maba: MabaDto, onUpdateStatus: (MabaDto) -> Unit) {
    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
        Text("Detail Calon Mahasiswa", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        InfoRow("Alamat", maba.alamatMaba)
        InfoRow("NIK", maba.nik)
        InfoRow("Prodi", maba.idProgramStudi.toString())

        Spacer(Modifier.height(24.dp))

        // Tombol Aksi Admin
        Button(
            onClick = { onUpdateStatus(maba.copy(statusVerifikasi = "Terverifikasi")) },
            modifier = Modifier.fillMaxWidth(),
            enabled = maba.statusVerifikasi != "Terverifikasi"
        ) { Text("Verifikasi Pendaftar") }

        Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { onUpdateStatus(maba.copy(statusKelulusan = "Lulus")) },
                modifier = Modifier.weight(1f)
            ) { Text("Lulus") }

            OutlinedButton(
                onClick = { onUpdateStatus(maba.copy(statusKelulusan = "Gugur")) },
                modifier = Modifier.weight(1f)
            ) { Text("Gugur") }
        }
        Spacer(Modifier.height(48.dp))
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun StatusBadge(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.2f), // Warna background transparan
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color) // Garis tepi solid
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}