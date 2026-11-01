package com.example.alfacasebot_0.network

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
data class LoginRequest(
    val email: String,
    val password: String
)
data class AuthResponse(
    val success: Boolean = false,
    val id: Int = -1,
    val username: String = "",
    val email: String = "",
    @SerializedName("access_token")
    val accessToken: String = "",
    @SerializedName("refresh_token")
    val refreshToken: String = "",
    val message: String = ""
)



data class SubmitCaseRequest(
    @SerializedName("case_id")
    val caseId: Int,
    val solution: String
)



data class RateCaseRequest(
    @SerializedName("case_id")
    val caseId: Int,
    val rating: Int
)


data class RefreshRequest(
    @SerializedName("refresh_token")
    val refreshToken: String
)



data class LogoutRequest(
    @SerializedName("refresh_token")
    val refreshToken: String
)

