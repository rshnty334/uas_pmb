package com.uas.pmb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uas.pmb.data.MabaDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 38 Provinsi Indonesia sesuai permintaan
val daftarProvinsi = listOf(
    "Aceh", "Sumatera Utara", "Sumatera Barat", "Riau", "Kepulauan Riau", "Jambi",
    "Sumatera Selatan", "Bangka Belitung", "Bengkulu", "Lampung", "DKI Jakarta",
    "Jawa Barat", "Banten", "Jawa Tengah", "DI Yogyakarta", "Jawa Timur",
    "Bali", "Nusa Tenggara Barat", "Nusa Tenggara Timur", "Kalimantan Barat",
    "Kalimantan Tengah", "Kalimantan Selatan", "Kalimantan Timur", "Kalimantan Utara",
    "Sulawesi Utara", "Sulawesi Tengah", "Sulawesi Selatan", "Sulawesi Tenggara",
    "Gorontalo", "Sulawesi Barat", "Maluku", "Maluku Utara", "Papua",
    "Papua Barat", "Papua Tengah", "Papua Pegunungan", "Papua Selatan", "Papua Barat Daya"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdiDropdown(selectedId: Long, onSelected: (Long) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    // Mapping ID sesuai database
    val prodiMap = mapOf(49401L to "D-III Statistika", 49501L to "D-IV Statistika", 49502L to "D-IV Komputasi Statistik")

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = prodiMap[selectedId] ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Program Studi") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            prodiMap.forEach { (id, nama) ->
                DropdownMenuItem(text = { Text(nama) }, onClick = { onSelected(id); expanded = false })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvinsiDropdown(selectedProv: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedProv,
            onValueChange = {},
            readOnly = true,
            label = { Text("Penempatan (Provinsi)") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            daftarProvinsi.forEach { prov ->
                DropdownMenuItem(text = { Text(prov) }, onClick = { onSelected(prov); expanded = false })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMabaScreen(navController: NavController, viewModel: PmbViewModel = hiltViewModel()) {
    val user by viewModel.profile.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile() // Pastikan ada fungsi ini di ViewModel kamu
    }

    // Date Picker State
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val date = datePickerState.selectedDateMillis?.let {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                    }
                    if (date != null) viewModel.mabaTglLahir = date
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Lengkapi Data Pendaftaran") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            OutlinedTextField(
                value = viewModel.mabaNik,
                onValueChange = { if(it.length <= 16) viewModel.mabaNik = it },
                label = { Text("NIK (16 Digit)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.mabaNama,
                onValueChange = { viewModel.mabaNama = it },
                label = { Text("Nama Lengkap") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Input Tanggal Lahir
            OutlinedTextField(
                value = viewModel.mabaTglLahir, onValueChange = { viewModel.mabaTglLahir = it }, readOnly = true, label = { Text("Tanggal Lahir") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { IconButton(onClick = { showDatePicker = true }) { Icon(Icons.Default.DateRange, null) } }
            )

            Spacer(Modifier.height(8.dp))

            // Input Jenis Kelamin (Radio Button Sederhana)
            Text("Jenis Kelamin", style = MaterialTheme.typography.labelLarge)

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = viewModel.mabaGender == "Laki-laki", onClick = { viewModel.mabaGender = "Laki-laki" })
                Text("Laki-laki")
                Spacer(Modifier.width(16.dp))
                RadioButton(selected = viewModel.mabaGender == "Perempuan", onClick = { viewModel.mabaGender = "Perempuan" })
                Text("Perempuan")
            }

            OutlinedTextField(value = viewModel.mabaAlamat, onValueChange = { viewModel.mabaAlamat = it }, label = { Text("Alamat Lengkap") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))

            ProdiDropdown(viewModel.mabaProdiId) { viewModel.mabaProdiId = it }
            Spacer(Modifier.height(8.dp))
            ProvinsiDropdown(viewModel.mabaProvinsi) { viewModel.mabaProvinsi = it }

            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    val currentEmail = user?.email
                    if (currentEmail != null) {
                        val maba = MabaDto(
                            nik = viewModel.mabaNik,
                            namaMaba = viewModel.mabaNama,
                            tglLahirMaba = viewModel.mabaTglLahir,
                            jenisKelaminMaba = viewModel.mabaGender,
                            alamatMaba = viewModel.mabaAlamat,
                            emailMaba = currentEmail, // Email diambil dari data user login
                            idProgramStudi = viewModel.mabaProdiId,
                            penempatanMaba = viewModel.mabaProvinsi
                        )
                        viewModel.addMaba(maba) {
                            viewModel.clearForm()
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.mabaNik.length == 16 && viewModel.mabaNama.isNotEmpty() && viewModel.mabaTglLahir.isNotEmpty() && user?.email != null
            ) { Text("Kirim Pendaftaran") }
        }
    }
}