package com.example.logistica.data.remote

import com.example.logistica.data.model.EntregaResponse
import com.example.logistica.data.model.EntregasResponse
import com.example.logistica.data.model.LoginRequest
import com.example.logistica.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ApiService {

    // Iniciar sesión
    @POST("login")
    suspend fun iniciarSesion(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // Cerrar sesión
    @POST("logout")
    suspend fun cerrarSesion(): Response<Unit>

    // Entregas pendientes y en camino del chofer
    @GET("entregas")
    suspend fun obtenerEntregas(): Response<EntregasResponse>

    // Cambiar entre pendiente y en_camino
    @PATCH("entregas/{id}/estado")
    suspend fun actualizarEstado(
        @Path("id") id: Int,
        @Body estado: Map<String, String>
    ): Response<EntregaResponse>


    // Registrar incidencia (motivo de 10 caracteres o más)
    @POST("entregas/{id}/incidencia")
    suspend fun registrarIncidencia(
        @Path("id") id: Int,
        @Body motivo: Map<String, String>
    ): Response<EntregaResponse>


    // Registrar entrega con evidencia (multipart)
    @Multipart
    @POST("entregas/{id}/entregar")
    suspend fun entregar(
        @Path("id") id: Int,
        @Part("receptor_nombre") receptorNombre: RequestBody,
        @Part fotos: List<MultipartBody.Part>,
        @Part firma: MultipartBody.Part?,
        @Part("motivo_sin_firma") motivoSinFirma: RequestBody?
    ): Response<EntregaResponse>
}