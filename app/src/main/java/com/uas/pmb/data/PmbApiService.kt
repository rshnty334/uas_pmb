package com.uas.pmb.data

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PmbApiService {
    @POST("login")
    suspend fun login(@Body creds: Map<String, String>): Response<AuthResponse>
    @POST("register")
    suspend fun register(@Body user: UserDto): Response<UserDto>

    @GET("api/mabas")
    suspend fun getMabas(@Header("Authorization") token: String): Response<List<MabaDto>>

    @POST("api/mabas")
    suspend fun addMaba(@Header("Authorization") token: String, @Body maba: MabaDto): Response<MabaDto>

    // PmbApiService.kt
    @PUT("api/mabas/{id}")
    suspend fun updateMaba(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body maba: MabaDto
    ): Response<MabaDto>

    @GET("api/profile/me")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserDto>

    @PUT("api/profile/password") // Sesuaikan path-nya dengan backend
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): Response<ResponseBody>

    @PUT("api/profile/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body user: UserDto
    ): Response<UserDto>

    @DELETE("api/profile/me")
    suspend fun deleteUser(
        @Header("Authorization") token: String
    ): Response<ResponseBody>

}