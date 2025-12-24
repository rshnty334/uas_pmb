package com.uas.pmb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uas.pmb.data.MabaDto
import com.uas.pmb.ui.screens.PmbViewModel

@Composable
fun AddMabaScreen(navController: NavController, viewModel: PmbViewModel = hiltViewModel()) {
    var nik by remember { mutableStateOf("") }
    var nama by remember { mutableStateOf("") }
    var prodiId by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Pendaftaran Maba", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value = nik, onValueChange = { nik = it }, label = { Text("NIK (16 Digit)") })
        OutlinedTextField(value = nama, onValueChange = { nama = it }, label = { Text("Nama Lengkap") })
        OutlinedTextField(value = prodiId, onValueChange = { prodiId = it }, label = { Text("ID Program Studi") })

        Button(onClick = {
            val maba = MabaDto(
                NIK = nik, namaMaba = nama, idProgramStudi = prodiId.toLong(),
                tglLahirMaba = "2005-01-01", jenisKelaminMaba = "Laki-laki",
                alamatMaba = "Bekasi", emailMaba = "test@stis.ac.id", penempatanMaba = "Jawa Barat"
            )
            viewModel.addMaba(maba) { navController.popBackStack() }
        }) {
            Text("Simpan Data")
        }
    }
}