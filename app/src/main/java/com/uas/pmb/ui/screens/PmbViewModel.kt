package com.uas.pmb.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.pmb.data.*
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
    fun login(email: String, pass: String, onOk: () -> Unit) {
        viewModelScope.launch {
            val res = repo.login(mapOf("email" to email, "password" to pass))
            if (res.isSuccessful) {
                res.body()?.accessToken?.let {
                    repo.saveToken(it)
                    onOk()
                }
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