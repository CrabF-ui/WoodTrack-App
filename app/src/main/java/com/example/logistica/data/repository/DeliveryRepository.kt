package com.example.logistica.data.repository

import com.example.logistica.data.model.Delivery
import com.example.logistica.data.model.LoginRequest
import com.example.logistica.data.remote.ApiService
import com.example.logistica.data.remote.TokenManager
import com.example.logistica.data.model.User
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

class DeliveryRepository(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {

    suspend fun iniciarSesion(
        email: String,
        password: String
    ): Result<User> {

        return try {
            val response = api.iniciarSesion(
                LoginRequest(email, password)
            )

            if (response.isSuccessful) {
                val resultado = response.body()
                    ?: return Result.failure(Exception("El servidor no devolvió información"))

                tokenManager.guardarToken(resultado.token)
                Result.success(resultado.user)
            } else {
                Result.failure(Exception(mensajeDeError(response)))
            }

        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar con el servidor. Revisa tu conexión."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerEntregas(): Result<List<Delivery>> {

        return try {
            val response = api.obtenerEntregas()

            if (response.isSuccessful) {

                val entregas = response.body() ?: emptyList()

                Result.success(entregas)

            } else {
                Result.failure(
                    Exception("No se pudieron obtener las entregas: ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerEntrega(id: Int): Result<Delivery> {

        return try {
            val response = api.obtenerEntrega(id)

            if (response.isSuccessful) {

                val entrega = response.body()
                    ?: return Result.failure(
                        Exception("No se encontró la entrega")
                    )

                Result.success(entrega)

            } else {
                Result.failure(
                    Exception("Error al consultar la entrega: ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarEstado(
        id: Int,
        nuevoEstado: String
    ): Result<String> {

        return try {
            val response = api.actualizarEstado(
                id,
                mapOf("estado" to nuevoEstado)
            )

            if (response.isSuccessful) {
                Result.success("Estado actualizado correctamente")
            } else {
                Result.failure(
                    Exception("No se pudo actualizar el estado: ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cerrarSesion(): Result<String> {

        return try {
            val response = api.cerrarSesion()

            if (response.isSuccessful) {
                tokenManager.eliminarToken()
                Result.success("Sesión cerrada correctamente")
            } else {
                Result.failure(
                    Exception("No se pudo cerrar la sesión: ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // Lee el "message" que manda Laravel (ya viene en español)
    private fun mensajeDeError(response: Response<*>): String {
        val cuerpo = response.errorBody()?.string()

        val mensaje = try {
            JSONObject(cuerpo ?: "").optString("message")
        } catch (e: Exception) {
            ""
        }

        return when {
            mensaje.isNotBlank() -> mensaje
            response.code() == 401 -> "Tu sesión expiró. Inicia sesión de nuevo."
            else -> "Error del servidor (${response.code()})"
        }
    }
}