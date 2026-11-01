package com.example.alfacasebot_0.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import okhttp3.ResponseBody

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("cases/submit")
    suspend fun submitCase(@Body request: SubmitCaseRequest): Response<ResponseBody>

    @GET("profile/stats")
    suspend fun getProfileStats(): Response<ResponseBody>

    @GET("rating/top")
    suspend fun getTopRating(): Response<ResponseBody>

    @GET("cases")
    suspend fun getCases(): Response<ResponseBody>

    @GET("cases/{caseId}/reference")
    suspend fun getReferenceSolution(
        @Path("caseId") caseId: Int
    ): Response<ResponseBody>

    @POST("cases/rate")
    suspend fun rateCase(@Body request: RateCaseRequest): Response<ResponseBody>

    @POST("auth/refresh")
    suspend fun refresh(
        @Body request: RefreshRequest
    ): Response<AuthResponse>

    @POST("auth/refresh")
    fun refreshBlocking(@Body request: RefreshRequest): retrofit2.Call<AuthResponse>

    @POST("logout")
    suspend fun logout(
        @Body request: LogoutRequest
    ): Response<ResponseBody>
}