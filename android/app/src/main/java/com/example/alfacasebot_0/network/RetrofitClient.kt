package com.example.alfacasebot_0.network

import android.content.Context
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val URL = "http://10.0.2.2:8000/"

    @Volatile
    private var apiService: ApiService? = null

    private val refreshLock = Any()

    fun getApiService(context: Context): ApiService {

        return apiService ?: synchronized(this) {
            apiService ?: run {
                val appContext = context.applicationContext
                val refreshClient = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()
                val refreshRetrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .client(refreshClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val refreshApi =
                    refreshRetrofit.create(ApiService::class.java)
                val authInterceptor = Interceptor { chain ->
                    val originalRequest = chain.request()
                    val path = originalRequest.url.encodedPath

                    val isAuthRequest =
                        path.endsWith("/login") ||
                                path.endsWith("/register") ||
                                path.endsWith("/auth/refresh")

                    val accessToken =
                        if (isAuthRequest) null
                        else Tokens.getAccess(appContext)

                    val newRequest = originalRequest.newBuilder().apply {
                        if (!accessToken.isNullOrBlank()) {
                            header(
                                "Authorization",
                                "Bearer $accessToken"
                            )
                        }
                    }.build()
                    chain.proceed(newRequest)
                }
                val tokenAuthenticator = object : Authenticator {
                    override fun authenticate(
                        route: Route?,
                        response: Response
                    ): okhttp3.Request? {
                        if (responseCount(response) >= 2) {
                            return null
                        }

                        synchronized(refreshLock) {
                            val currentAccess =
                                Tokens.getAccess(appContext)

                            val requestAccess = response.request
                                .header("Authorization")
                                ?.removePrefix("Bearer ")
                            if (!currentAccess.isNullOrBlank() &&
                                currentAccess != requestAccess
                            ) {
                                return response.request.newBuilder()
                                    .header(
                                        "Authorization",
                                        "Bearer $currentAccess"
                                    )
                                    .build()
                            }

                            val refreshToken =
                                Tokens.getRefresh(appContext)

                            if (refreshToken.isNullOrBlank()) {
                                return null
                            }

                            try {
                                val refreshResponse =
                                    refreshApi.refreshBlocking(
                                        RefreshRequest(refreshToken)
                                    ).execute()
                                if (!refreshResponse.isSuccessful) {
                                    if (refreshResponse.code() == 401 ||
                                        refreshResponse.code() == 400
                                    ) {
                                        Tokens.clear(appContext)
                                    }

                                    return null
                                }
                                val auth = refreshResponse.body()
                                    ?: return null
                                if (!auth.success ||
                                    auth.accessToken.isBlank() ||
                                    auth.refreshToken.isBlank()
                                ) {
                                    return null
                                }
                                Tokens.save(
                                    appContext,
                                    auth.accessToken,
                                    auth.refreshToken
                                )
                                return response.request.newBuilder()
                                    .header(
                                        "Authorization",
                                        "Bearer ${auth.accessToken}"
                                    )
                                    .build()
                            } catch (e: Exception) {
                                return null
                            }
                        }
                    }
                    private fun responseCount(response: Response): Int {
                        var count = 1
                        var prior = response.priorResponse
                        while (prior != null) {
                            count++
                            prior = prior.priorResponse
                        }
                        return count
                    }
                }
                val client = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(authInterceptor)
                    .authenticator(tokenAuthenticator)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory(
                        GsonConverterFactory.create()
                    )
                    .build()
                retrofit.create(ApiService::class.java).also {
                    apiService = it
                }
            }
        }
    }
}