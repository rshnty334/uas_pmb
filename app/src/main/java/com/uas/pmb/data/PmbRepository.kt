package com.uas.pmb.data

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PmbRepository @Inject constructor(
    private val api: PmbApiService,
    private val tokenManager: TokenManager
) {
    // Flow token untuk dicek di ViewModel atau MainActivity
    val token: Flow<String?> = tokenManager.token

    suspend fun login(creds: Map<String, String>): Response<AuthResponse> {
        val response = api.login(creds)
        if (response.isSuccessful) {
            response.body()?.accessToken?.let {
                tokenManager.saveToken(it) // Simpan otomatis saat login sukses
            }
        }
        return response
    }

    suspend fun register(user: UserDto) = api.register(user)

    suspend fun getMabas(token: String) = api.getMabas("Bearer $token")

    suspend fun addMaba(token: String, maba: MabaDto) = api.addMaba("Bearer $token", maba)

    suspend fun updateMaba(token: String, maba: MabaDto): Response<MabaDto> {
        // Mengirim token dengan prefix Bearer dan ID maba yang akan diupdate
        return api.updateMaba("Bearer $token", maba.idMaba ?: 0L, maba)
    }

    suspend fun changePassword(token: String, request: ChangePasswordRequest): Response<ResponseBody> {
        return api.changePassword("Bearer $token", request)
    }


    suspend fun getProfile(token: String): Response<UserDto> {
        return api.getProfile("Bearer $token")
    }

    suspend fun updateProfile(token: String, user: UserDto): Response<UserDto> {
        return api.updateProfile("Bearer $token", user)
    }

    suspend fun deleteUser(token: String) = api.deleteUser("Bearer $token")

    suspend fun logout() = tokenManager.clearToken()

}