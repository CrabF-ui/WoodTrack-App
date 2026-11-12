package com.example.logistica.data.remote

import com.example.logistica.data.model.Delivery
import com.example.logistica.data.model.LoginRequest
import com.example.logistica.data.model.LoginResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // Iniciar sesión
    @POST("login")
    suspend fun iniciarSesion(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // Cerrar sesión
    @POST("logout")
    suspend fun cerrarSesion(): Response<Unit>

    // Consultar entregas
    @GET("entregas")
    suspend fun obtenerEntregas(): Response<List<Delivery>>

    // Consultar una entrega
    @GET("entregas/{id}")
    suspend fun obtenerEntrega(
        @Path("id") id: Int
    ): Response<Delivery>

    // Actualizar estado de entrega
    @PUT("entregas/{id}/estado")
    suspend fun actualizarEstado(
        @Path("id") id: Int,
        @Body estado: Map<String, String>
    ): Response<Unit>
}