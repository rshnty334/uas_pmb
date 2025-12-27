package com.uas.pmb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uas.pmb.data.UserDto

@Composable
fun AccountView(user: UserDto?, viewModel: PmbViewModel, navController: NavController) {
    var newName by remember { mutableStateOf(user?.nama ?: "") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    val statusMessage by viewModel.statusMessage.collectAsState()

    // Gunakan verticalScroll agar tidak error di layar kecil
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(48.dp))
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(text = user?.nama ?: "Loading...", style = MaterialTheme.typography.headlineSmall)
                    Text(text = user?.role ?: "-", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(text = "Ganti Nama Pengguna Akun", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Nama Pengguna Akun Baru") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { viewModel.updateProfile(newName) }, modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Simpan Nama Baru")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(text = "Ganti Password", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = oldPassword,
            onValueChange = { oldPassword = it },
            label = { Text("Password Lama") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Password Baru") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.changePassword(oldPassword, newPassword) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Password")
        }

        // Tampilkan feedback dari API
        statusMessage?.let {
            Text(
                text = it,
                color = if(it.contains("successfully", ignoreCase = true)) Color(0xFF4CAF50) else Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // TOMBOL HAPUS AKUN
        TextButton(
            onClick = { showDeleteDialog = true },
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Hapus Akun Permanen")
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Hapus Akun?") },
                text = { Text("Semua data pendaftaran Anda akan ikut terhapus. Tindakan ini tidak bisa dibatalkan.") },
                confirmButton = {
                    Button(
                        onClick = { viewModel.deleteUser { navController.navigate("login") { popUpTo(0) } } },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) { Text("Hapus") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
                }
            )
        }
//        Spacer(modifier = Modifier.height(32.dp))
//        OutlinedButton(
//            onClick = {
//                viewModel.logout {
//                    navController.navigate("login") { popUpTo(0) }
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
//        ) {
//            Text("Keluar Akun")
//        }
    }
}