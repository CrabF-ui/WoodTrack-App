package com.example.logistica

import android.content.Intent
import android.net.Uri
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

import com.example.logistica.navigation.AppNavigation
import com.example.logistica.ui.theme.Fondo

import com.example.logistica.data.remote.TokenManager
import com.example.logistica.data.remote.RetrofitClient
import com.example.logistica.data.repository.DeliveryRepository

class MainActivity : ComponentActivity() {

    // Objetos para la conexión con la API
    private lateinit var tokenManager: TokenManager
    private lateinit var apiService: com.example.logistica.data.remote.ApiService
    private lateinit var repository: DeliveryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar conexión con el backend
        tokenManager = TokenManager(applicationContext)

        apiService = RetrofitClient.crearApi(tokenManager)

        repository = DeliveryRepository(
            apiService,
            tokenManager
        )

        // Color de la barra superior
        window.statusBarColor = android.graphics.Color.rgb(
            245, 239, 229
        )

        // Color de la barra inferior
        window.navigationBarColor = android.graphics.Color.rgb(
            245, 239, 229
        )

        // Iconos oscuros en las barras del sistema
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        // Interfaz de la aplicación
        setContent {

            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF70452F),
                    secondary = Color(0xFFB87524),
                    background = Fondo,
                    surface = Color.White
                )
            ) {

                Surface(
                    color = Fondo
                ) {

                    AppNavigation(
                        abrirMapa = {
                            abrirGoogleMaps()
                        }
                    )
                }
            }
        }
    }

    // Abrir Google Maps
    private fun abrirGoogleMaps() {

        val direccion = Uri.encode(
            "Av. Las Torres 184, Col. Centro, Guadalajara, Jalisco"
        )

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                "https://www.google.com/maps/search/?api=1&query=$direccion"
            )
        )

        startActivity(intent)
    }
}
```