package com.uas.pmb.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: PmbViewModel = hiltViewModel() // Menggunakan ViewModel utama
) {

    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError() // Hapus pesan agar tidak muncul berulang
        }
    }

    LaunchedEffect(viewModel.loginSuccess) {
        if (viewModel.loginSuccess) {
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
            }
            viewModel.resetLoginState()
        }
    }

    // State untuk menampung input user
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "PMB Polstat STIS",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), // Agar password tidak terlihat
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Login yang diperbaiki
        Button(
            onClick = {
                // HANYA panggil fungsi login dengan 2 parameter
                viewModel.login(email, password)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Masuk")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Opsional: Tombol Register jika kamu membuat fiturnya
        TextButton(onClick = { navController.navigate("register") }) {
            Text("Belum punya akun? Daftar sekarang")
        }
    }
}