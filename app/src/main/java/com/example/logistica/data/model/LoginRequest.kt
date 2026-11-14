package com.example.logistica.data.model

data class LoginRequest(
    val email: String,
    val password: String,
    val device_name: String = "android"
)