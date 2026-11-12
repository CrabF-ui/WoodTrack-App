package com.example.logistica.data.model

data class Delivery(
    val id: Int,
    val cliente: String,
    val direccion: String,
    val telefono: String?,
    val estado: String,
    val fecha: String?
)