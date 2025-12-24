package com.uas.pmb.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PmbApiService {
    @POST("register")
    suspend fun register(@Body user: UserDto): Response<UserDto>

    @POST("login")
    suspend fun login(@Body creds: Map<String, String>): Response<Map<String, String>>

    @GET("api/mabas")
    suspend fun getMabas(@Header("Authorization") token: String): List<MabaDto>

    @POST("api/mabas")
    suspend fun addMaba(@Header("Authorization") token: String, @Body maba: MabaDto): Response<MabaDto>

    @GET("api/profile/me")
    suspend fun getProfile(@Header("Authorization") token: String): UserDto
}