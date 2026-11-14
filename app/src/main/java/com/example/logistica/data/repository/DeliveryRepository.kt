package com.example.logistica.data.repository

import com.example.logistica.data.model.Delivery
import com.example.logistica.data.model.LoginRequest
import com.example.logistica.data.model.User
import com.example.logistica.data.remote.ApiService
import com.example.logistica.data.remote.TokenManager
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class DeliveryRepository(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {

    // ---------- LOGIN ----------
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
                tokenManager.guardarNombre(resultado.user.name)
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

    fun nombreUsuario(): String? = tokenManager.obtenerNombre()

    // ---------- ENTREGAS ----------
    suspend fun obtenerEntregas(): Result<List<Delivery>> {

        return try {
            val response = api.obtenerEntregas()

            if (response.isSuccessful) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception(mensajeDeError(response)))
            }

        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar con el servidor. Revisa tu conexión."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- ESTADO E INCIDENCIA ----------
    suspend fun actualizarEstado(
        id: Int,
        nuevoEstado: String
    ): Result<Delivery> {

        return try {
            val response = api.actualizarEstado(
                id,
                mapOf("estado" to nuevoEstado)
            )

            if (response.isSuccessful) {
                val entrega = response.body()?.data
                    ?: return Result.failure(Exception("El servidor no devolvió información"))
                Result.success(entrega)
            } else {
                Result.failure(Exception(mensajeDeError(response)))
            }

        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar con el servidor. Revisa tu conexión."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registrarIncidencia(
        id: Int,
        motivo: String
    ): Result<Delivery> {

        return try {
            val response = api.registrarIncidencia(
                id,
                mapOf("motivo" to motivo)
            )

            if (response.isSuccessful) {
                val entrega = response.body()?.data
                    ?: return Result.failure(Exception("El servidor no devolvió información"))
                Result.success(entrega)
            } else {
                Result.failure(Exception(mensajeDeError(response)))
            }

        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar con el servidor. Revisa tu conexión."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- ENTREGAR CON EVIDENCIA ----------
    suspend fun entregar(
        id: Int,
        receptorNombre: String,
        fotos: List<File>,
        firma: File?,
        motivoSinFirma: String?
    ): Result<Unit> {

        return try {
            val texto = "text/plain".toMediaType()

            // Cada foto va con el nombre "fotos[]", como pide la API
            val partesFotos = fotos.map { archivo ->
                MultipartBody.Part.createFormData(
                    "fotos[]",
                    archivo.name,
                    archivo.asRequestBody("image/jpeg".toMediaType())
                )
            }

            val parteFirma = firma?.let { archivo ->
                MultipartBody.Part.createFormData(
                    "firma",
                    archivo.name,
                    archivo.asRequestBody("image/png".toMediaType())
                )
            }

            val response = api.entregar(
                id,
                receptorNombre.toRequestBody(texto),
                partesFotos,
                parteFirma,
                motivoSinFirma?.toRequestBody(texto)
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val mensaje = mensajeDeError(response)

                // Si un intento anterior sí llegó al servidor, la entrega ya quedó
                // registrada. La guía dice tratarlo como éxito.
                if (response.code() == 422 && mensaje.contains("ya fue cerrada")) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(mensaje))
                }
            }

        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar con el servidor. Intenta de nuevo."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- CERRAR SESIÓN (se termina en la Parte 6) ----------
    suspend fun cerrarSesion(): Result<String> {

        return try {
            val response = api.cerrarSesion()

            if (response.isSuccessful) {
                tokenManager.eliminarToken()
                Result.success("Sesión cerrada correctamente")
            } else {
                Result.failure(Exception(mensajeDeError(response)))
            }

        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar con el servidor. Revisa tu conexión."))
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