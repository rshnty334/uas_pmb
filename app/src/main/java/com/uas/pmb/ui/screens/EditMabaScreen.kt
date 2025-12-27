package com.uas.pmb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uas.pmb.ui.theme.StisBluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMabaScreen(navController: NavController, viewModel: PmbViewModel = hiltViewModel()) {
    val user by viewModel.profile.collectAsState()
    val mabaList by viewModel.mabaList.collectAsState()

    // Mencari data asli maba berdasarkan email user yang login
    val myData = mabaList.find { it.emailMaba == user?.email }

    // State untuk semua field
    var nikState by remember { mutableStateOf("") }
    var namaState by remember { mutableStateOf("") }
    var alamatState by remember { mutableStateOf("") }
    var tglLahirState by remember { mutableStateOf("") }
    var genderState by remember { mutableStateOf("") }
    var selectedProdiId by remember { mutableLongStateOf(49501L) }
    var selectedProvinsi by remember { mutableStateOf("DKI Jakarta") }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        viewModel.loadMabas()
    }

    LaunchedEffect(myData) {
        myData?.let {
            nikState = it.nik
            namaState = it.namaMaba
            alamatState = it.alamatMaba
            tglLahirState = it.tglLahirMaba
            genderState = it.jenisKelaminMaba
            selectedProdiId = it.idProgramStudi
            selectedProvinsi = it.penempatanMaba
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Data Pendaftaran") },
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
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. NIK (TERLOCK)
            OutlinedTextField(
                value = nikState,
                onValueChange = {},
                label = { Text("NIK") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true, // Dikunci
                colors = OutlinedTextFieldDefaults.colors(
                    // Warna saat tidak diklik (langsung muncul)
                    unfocusedContainerColor = Color(0xFFDFDFDF),
                    // Warna saat diklik (dibuat sama agar konsisten)
                    focusedContainerColor = Color(0xFFDFDFDF),
                    // Opsional: Atur warna border agar lebih serasi
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = StisBluePrimary
                )
            )

            Spacer(Modifier.height(8.dp))

            // 2. Nama Lengkap (TERLOCK)
            OutlinedTextField(
                value = namaState,
                onValueChange = {},
                label = { Text("Nama Lengkap") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true, // Dikunci
                colors = OutlinedTextFieldDefaults.colors(
                    // Warna saat tidak diklik (langsung muncul)
                    unfocusedContainerColor = Color(0xFFDFDFDF),
                    // Warna saat diklik (dibuat sama agar konsisten)
                    focusedContainerColor = Color(0xFFDFDFDF),
                    // Opsional: Atur warna border agar lebih serasi
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = StisBluePrimary
                )
            )

            Spacer(Modifier.height(8.dp))

            // 5. Tanggal Lahir (EDITABLE)
            OutlinedTextField(
                value = tglLahirState,
                onValueChange = { },
                label = { Text("Tanggal Lahir (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    // Warna saat tidak diklik (langsung muncul)
                    unfocusedContainerColor = Color(0xFFDFDFDF),
                    // Warna saat diklik (dibuat sama agar konsisten)
                    focusedContainerColor = Color(0xFFDFDFDF),
                    // Opsional: Atur warna border agar lebih serasi
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = StisBluePrimary
                )
            )

            Spacer(Modifier.height(8.dp))

            // 3. Jenis Kelamin (TERLOCK)
            OutlinedTextField(
                value = genderState,
                onValueChange = {},
                label = { Text("Jenis Kelamin") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true, // Dikunci
                colors = OutlinedTextFieldDefaults.colors(
                    // Warna saat tidak diklik (langsung muncul)
                    unfocusedContainerColor = Color(0xFFDFDFDF),
                    // Warna saat diklik (dibuat sama agar konsisten)
                    focusedContainerColor = Color(0xFFDFDFDF),
                    // Opsional: Atur warna border agar lebih serasi
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = StisBluePrimary
                )
            )

            Spacer(Modifier.height(8.dp))

            // 4. Alamat (EDITABLE)
            OutlinedTextField(
                value = alamatState,
                onValueChange = { alamatState = it },
                label = { Text("Alamat Lengkap") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // 6. Program Studi (EDITABLE via Dropdown)
            Text("Pilihan Program Studi", style = MaterialTheme.typography.labelMedium)
            ProdiDropdown(selectedProdiId) { selectedProdiId = it }

            Spacer(Modifier.height(16.dp))

            // 7. Provinsi/Penempatan (EDITABLE via Dropdown)
            Text("Provinsi Penempatan", style = MaterialTheme.typography.labelMedium)
            ProvinsiDropdown(selectedProvinsi) { selectedProvinsi = it }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    val updatedMaba = myData?.copy(
                        alamatMaba = alamatState,
                        idProgramStudi = selectedProdiId,
                        penempatanMaba = selectedProvinsi
                    )
                    if (updatedMaba != null) {
                        viewModel.updateMabaStatus(updatedMaba)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Perubahan")
            }
        }
    }
}