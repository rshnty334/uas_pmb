package com.uas.pmb.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.pmb.data.*
import com.uas.pmb.network.NetworkModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PmbViewModel @Inject constructor(private val repo: PmbRepository) : ViewModel() {
    private val _mabaList = MutableStateFlow<List<MabaDto>>(emptyList())
    val mabaList = _mabaList.asStateFlow()

    private val _profile = MutableStateFlow<UserDto?>(null)
    val profile = _profile.asStateFlow()

    // Fungsi Login yang tadinya di LoginViewModel pindah ke sini
    fun login(email: String, password: String, onOk: () -> Unit) {
        viewModelScope.launch {
            try {
                // Sesuai dengan interface kamu yang menggunakan Map<String, String>
                val creds = mapOf("email" to email, "password" to password)

                val response = NetworkModule.apiService.login(creds)

                if (response.isSuccessful) {
                    val authResponse = response.body()
                    println("Login Berhasil: ${authResponse?.accessToken}")
                    // Simpan token ke DataStore/SharedPrefs
                } else {
                    // Ini menangani 401 Unauthorized
                    println("Login Gagal: Kode ${response.code()}")
                }
            } catch (e: Exception) {
                // Ini menangani SocketTimeoutException
                println("Error Jaringan: ${e.message}")
            }
        }
    }

    // Tambahkan ini di PmbViewModel.kt
    fun register(user: UserDto, onOk: () -> Unit) {
        viewModelScope.launch {
            try {
                // Pastikan di Repository kamu sudah ada fungsi register
                val res = repo.register(user)
                if (res.isSuccessful) {
                    onOk()
                } else {
                    println("Registrasi Gagal: ${res.code()}")
                }
            } catch (e: Exception) {
                println("Error Jaringan: ${e.message}")
            }
        }
    }


    fun loadMabas() {
        viewModelScope.launch {
            repo.token.first()?.let { _mabaList.value = repo.getMabas(it) }
        }
    }
    // ... fungsi lainnya (addMaba, loadProfile, logout) tetap ada di sini

    fun addMaba(maba: MabaDto, onOk: () -> Unit) {
        viewModelScope.launch {
            repo.token.first()?.let {
                if(repo.addMaba(it, maba).isSuccessful) onOk()
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            repo.token.first()?.let { _profile.value = repo.getProfile(it) }
        }
    }

    fun logout(onOk: () -> Unit) {
        viewModelScope.launch { repo.logout(); onOk() }
    }
}