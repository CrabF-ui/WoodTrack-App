package com.example.logistica.data.remote

import android.content.Context

class TokenManager(context: Context) {

    private val preferences = context.getSharedPreferences(
        "logistica_preferences",
        Context.MODE_PRIVATE
    )

    fun guardarToken(token: String) {
        preferences.edit()
            .putString("auth_token", token)
            .apply()
    }

    fun obtenerToken(): String? {
        return preferences.getString("auth_token", null)
    }

    fun eliminarToken() {
        preferences.edit()
            .remove("auth_token")
            .apply()
    }
}