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

    fun guardarNombre(nombre: String) {
        preferences.edit()
            .putString("user_name", nombre)
            .apply()
    }

    fun obtenerNombre(): String? {
        return preferences.getString("user_name", null)
    }

    // IDs de las entregas que el chofer ya vio (para no notificarlas)
    fun guardarIdsVistos(ids: Set<Int>) {
        preferences.edit()
            .putStringSet("ids_vistos", ids.map { it.toString() }.toSet())
            .apply()
    }

    // null = todavía no se ha revisado nunca
    fun obtenerIdsVistos(): Set<Int>? {
        return preferences.getStringSet("ids_vistos", null)
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet()
    }

    fun eliminarToken() {
        preferences.edit()
            .remove("auth_token")
            .remove("user_name")
            .remove("ids_vistos")
            .apply()
    }
}