package com.example.logistica.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
object RetrofitClient {

    // Dirección local de ejemplo.
    // Cambiarla cuando conozcas la dirección real del backend.
    private const val BASE_URL = "http://192.168.100.11:8000/api/"
    //

    fun crearApi(tokenManager: TokenManager): ApiService {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->

                val solicitudOriginal = chain.request()

                val token = tokenManager.obtenerToken()

                val solicitudNueva = solicitudOriginal.newBuilder()
                    .apply {
                        if (!token.isNullOrBlank()) {
                            addHeader(
                                "Authorization",
                                "Bearer $token"
                            )
                        }
                    }
                    .addHeader("Accept", "application/json")
                    .build()

                chain.proceed(solicitudNueva)
            }
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}