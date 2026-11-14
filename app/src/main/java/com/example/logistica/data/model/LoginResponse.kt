package com.example.logistica.data.model

data class LoginResponse(
    val token: String,
    val token_type: String,
    val expires_at: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val email: String
)