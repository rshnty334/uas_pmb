package com.uas.pmb.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.pmb.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Response

@HiltViewModel
class PmbViewModel @Inject constructor(private val repo: PmbRepository) : ViewModel() {
    var mabaNik by mutableStateOf("")
    var mabaNama by mutableStateOf("")
    var mabaAlamat by mutableStateOf("")
    var mabaTglLahir by mutableStateOf("")
    var mabaProdiId by mutableStateOf(49501L)
    var mabaProvinsi by mutableStateOf("DKI Jakarta")
    var mabaGender by mutableStateOf("Laki-laki")

    private val _mabaList = MutableStateFlow<List<MabaDto>>(emptyList())
    val mabaList = _mabaList.asStateFlow()

    private val _profile = MutableStateFlow<UserDto?>(null)
    val profile = _profile.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    val isUserLoggedIn = _isUserLoggedIn.asStateFlow()

    // State untuk Navigasi
    var loginSuccess by mutableStateOf(false)
        private set

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage = _statusMessage.asStateFlow()

    fun checkLoginStatus() {
        viewModelScope.launch {
            // Ambil token pertama yang tersedia
            val token = repo.token.first()
            _isUserLoggedIn.value = !token.isNullOrEmpty()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val creds = mapOf("email" to email, "password" to password)
                val response = repo.login(creds)

                if (response.isSuccessful) {
                    _errorMessage.value = null // Reset error jika berhasil
                    loginSuccess = true
                } else if (response.code() == 401) {
                    // Tangani error 401 (Bad Credentials)
                    _errorMessage.value = "Email atau Password salah!"
                } else {
                    _errorMessage.value = "Terjadi kesalahan server: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Koneksi gagal: ${e.message}"
            }
        }
    }

    // Pastikan parameter onOk ada agar RegisterScreen bisa pindah halaman
    fun register(user: UserDto, onOk: () -> Unit) {
        viewModelScope.launch {
            try {
                if (repo.register(user).isSuccessful) onOk()
            } catch (_: Exception) { /* handle */ }
        }
    }

    // Pastikan parameter onOk ada agar AddMabaScreen bisa popBackStack
    fun addMaba(maba: MabaDto, onOk: () -> Unit) {
        viewModelScope.launch {
            try {
                repo.token.first()?.let {
                    if (repo.addMaba(it, maba).isSuccessful) onOk()
                }
            } catch (e: Exception) {
                println("Error Add Maba: ${e.message}") // Peringatan hilang karena 'e' digunakan
            }
        }
    }

    fun loadMabas() {
        viewModelScope.launch {
            try {
                repo.token.first()?.let { token ->
                    val response: Response<List<MabaDto>> = repo.getMabas(token)

                    if (response.isSuccessful) {
                        _mabaList.value = response.body() ?: emptyList()
                    }
                }
            } catch (e: Exception) {
                println("Error Load Mabas: ${e.message}")
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            try {
                val token = repo.token.first()
                if (!token.isNullOrEmpty()) {
                    val response = repo.getProfile(token)

                    // Tambahkan Log untuk Debugging
                    println("DEBUG_PROFILE: Code ${response.code()}, Body: ${response.body()}")

                    if (response.isSuccessful) {
                        _profile.value = response.body()
                    } else {
                        println("DEBUG_PROFILE: Gagal ambil profil - ${response.errorBody()?.string()}")
                        _profile.value = null
                    }
                } else {
                    println("DEBUG_PROFILE: Token tidak ditemukan di DataStore")
                }
            } catch (e: Exception) {
                println("DEBUG_PROFILE: Exception - ${e.message}")
                _profile.value = null
            }
        }
    }

    fun resetLoginState() { loginSuccess = false }

    fun changePassword(oldPw: String, newPw: String) {
        viewModelScope.launch {
            try {
                val token = repo.token.first()
                if (token != null) {
                    val response = repo.changePassword(token, ChangePasswordRequest(oldPw, newPw, newPw))

                    if (response.isSuccessful) {
                        // AMAN: Baca string sekali saja
                        val rawMessage = response.body()?.string()
                        _statusMessage.value = rawMessage ?: "Berhasil"
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Gagal"
                        _statusMessage.value = errorMsg
                    }
                }
            } catch (e: Exception) {
                // Ini mencegah aplikasi Force Close jika network error
                _statusMessage.value = "Error: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }

    // Di dalam PmbViewModel
    fun updateProfile(newName: String) {
        viewModelScope.launch {
            try {
                val token = repo.token.first()
                val currentProfile = _profile.value // Ambil data profil saat ini

                if (token != null && currentProfile != null) {
                    // Buat objek DTO baru dengan nama yang diupdate
                    val updatedDto = currentProfile.copy(nama = newName)

                    val response = repo.updateProfile(token, updatedDto)
                    if (response.isSuccessful) {
                        // Update state profil secara lokal agar Card langsung berubah
                        _profile.value = response.body()
                        _statusMessage.value = "Nama berhasil diperbarui!"
                    } else {
                        _statusMessage.value = "Gagal: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _statusMessage.value = "Koneksi gagal: ${e.message}"
            }
        }
    }

    fun deleteUser(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repo.token.first()?.let { token ->
                val response = repo.deleteUser(token)
                if (response.isSuccessful) {
                    repo.logout() // Hapus token dari TokenManager
                    onDeleted()   // Navigasi kembali ke LoginScreen
                }
            }
        }
    }

    fun updateMabaStatus(maba: MabaDto) {
        viewModelScope.launch {
            try {
                repo.token.first()?.let { token ->
                    val response = repo.updateMaba(token, maba)
                    if (response.isSuccessful) {
                        // Muat ulang data setelah update berhasil
                        loadMabas()
                    }
                }
            } catch (e: Exception) {
                println("Error Update: ${e.message}")
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadMabas() // Memuat ulang list maba
            loadProfile() // Memuat ulang profil
            _isRefreshing.value = false
        }
    }

    fun clearForm() {
        mabaNik = ""; mabaNama = ""; mabaAlamat = ""; mabaTglLahir = ""
    }

    fun clearError() { _errorMessage.value = null }

    fun logout(onOk: () -> Unit) {
        viewModelScope.launch {
            repo.logout()
            onOk()
        }
    }
}