package com.uas.pmb.ui.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.pmb.data.PmbApiService
import com.uas.pmb.data.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: PmbApiService,
    private val tokenManager: TokenManager
) : ViewModel() {
    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val response = api.login(mapOf("email" to email, "password" to pass))
            if (response.isSuccessful) {
                response.body()?.get("accessToken")?.let {
                    tokenManager.saveToken(it)
                    onSuccess()
                }
            }
        }
    }
}