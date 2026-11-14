package com.example.logistica.com.example.logistica

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import com.example.logistica.data.remote.ApiService
import com.example.logistica.data.remote.RetrofitClient
import com.example.logistica.data.remote.TokenManager
import com.example.logistica.data.repository.DeliveryRepository
import com.example.logistica.navigation.AppNavigation
import com.example.logistica.ui.theme.Fondo

class MainActivity : ComponentActivity() {

    // Objetos para la conexión con la API
    private lateinit var tokenManager: TokenManager
    private lateinit var apiService: ApiService
    private lateinit var repository: DeliveryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Barras del sistema transparentes con iconos oscuros (edge-to-edge)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

        // Inicializar conexión con el backend
        tokenManager = TokenManager(applicationContext)

        apiService = RetrofitClient.crearApi(tokenManager)

        repository = DeliveryRepository(
            apiService,
            tokenManager
        )

        // Interfaz de la aplicación
        setContent {

            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = androidx.compose.ui.graphics.Color(0xFF70452F),
                    secondary = androidx.compose.ui.graphics.Color(0xFFB87524),
                    background = Fondo,
                    surface = androidx.compose.ui.graphics.Color.White
                )
            ) {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Fondo
                ) {

                    AppNavigation(
                        repository = repository,
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