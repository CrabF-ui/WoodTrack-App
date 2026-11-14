package com.example.logistica.data.remote

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Avisa a la app cuando la API responde 401 (token vencido o revocado)
object SesionEvents {

    private val _expirada = MutableStateFlow(false)
    val expirada: StateFlow<Boolean> = _expirada

    fun notificarExpirada() {
        _expirada.value = true
    }

    fun reiniciar() {
        _expirada.value = false
    }
}