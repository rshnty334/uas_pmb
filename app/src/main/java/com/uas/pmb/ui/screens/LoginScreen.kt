package com.uas.pmb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uas.pmb.ui.component.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Login PMB STIS", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Button(onClick = { viewModel.performLogin(email, password) }) {
            Text("Masuk")
        }

        TextButton(onClick = { navController.navigate("register") }) {
            Text("Belum punya akun? Daftar")
        }
    }
}