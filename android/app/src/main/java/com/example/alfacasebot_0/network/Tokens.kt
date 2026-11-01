package com.example.alfacasebot_0.network

import android.content.Context
import android.content.SharedPreferences

object Tokens {

    private const val TOKENS = "tokens"
    private const val ACCESS_TOKEN = "access_token"
    private const val REFRESH_TOKEN = "refresh_token"

    private fun getPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(TOKENS, Context.MODE_PRIVATE)
    }
    fun save(context: Context, accessToken: String, refreshToken: String) {
        getPref(context).edit()
            .putString(ACCESS_TOKEN, accessToken)
            .putString(REFRESH_TOKEN, refreshToken)
            .apply()
    }
    fun getAccess(context: Context): String? =
        getPref(context).getString(ACCESS_TOKEN, null)


    fun getRefresh(context: Context): String? =
        getPref(context).getString(REFRESH_TOKEN, null)

    fun clear(context: Context) {
        getPref(context).edit().clear().apply()
    }
}
