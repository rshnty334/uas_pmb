package com.uas.pmb.data

import javax.inject.Inject

class PmbRepository @Inject constructor(
    private val api: PmbApiService,
    private val tokenManager: TokenManager
) {
    val token = tokenManager.token

    suspend fun login(creds: Map<String, String>) = api.login(creds)

    suspend fun register(user: UserDto) = api.register(user)

    suspend fun getMabas(token: String) = api.getMabas(token)

    suspend fun addMaba(token: String, maba: MabaDto) = api.addMaba(token, maba)

    suspend fun getProfile(token: String) = api.getProfile(token)

    suspend fun saveToken(t: String) = tokenManager.saveToken(t)

    suspend fun logout() = tokenManager.deleteToken()
}