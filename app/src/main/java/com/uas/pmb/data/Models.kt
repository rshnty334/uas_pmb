package com.uas.pmb.data

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val accessToken: String,
    val email: String,
    val role: String
)

data class UserDto(
    val id: Long? = null,
    @SerializedName("name") val nama: String? = null,
    val email: String,
    val password: String? = null,
    val role: String? = null
)

data class MabaDto(
    val idMaba: Long? = null,
    @SerializedName("NIK") val nik: String, // Gunakan huruf kecil sesuai standar JSON Jackson
    @SerializedName("namaMaba") val namaMaba: String,
    @SerializedName("tglLahirMaba") val tglLahirMaba: String,
    @SerializedName("jenisKelaminMaba") val jenisKelaminMaba: String,
    @SerializedName("alamatMaba") val alamatMaba: String,
    @SerializedName("emailMaba") val emailMaba: String,
    @SerializedName("idProgramStudi") val idProgramStudi: Long,
    @SerializedName("penempatanMaba") val penempatanMaba: String,
    val statusVerifikasi: String = "BELUM_DIVERIFIKASI",
    val statusKelulusan: String = "PENDING"
)

data class ChangePasswordRequest(
    val old_password: String,
    val new_password: String,
    val confirm_password: String
)