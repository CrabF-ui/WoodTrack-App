package com.example.logistica.data.model

data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val token: String,
    val user: User
)

data class User(
    val id: Int,
    val nombre: String?,
    val email: String?,
    val rol: String?
)