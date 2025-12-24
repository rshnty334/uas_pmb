package com.uas.pmb.data

import com.google.gson.annotations.SerializedName

data class UserDto(
    val id: Long? = null,
    val nama: String? = null,
    val email: String,
    val password: String? = null,
    val role: String? = null
)

data class MabaDto(
    val idMaba: Long? = null,
    @SerializedName("NIK") val NIK: String,
    val namaMaba: String,
    val tglLahirMaba: String,
    val jenisKelaminMaba: String,
    val alamatMaba: String,
    val emailMaba: String,
    val idProgramStudi: Long,
    val penempatanMaba: String,
    val statusVerifikasi: String? = null,
    val statusKelulusan: String? = null
)

data class AuthResponse(val accessToken: String, val email: String, val role: String)