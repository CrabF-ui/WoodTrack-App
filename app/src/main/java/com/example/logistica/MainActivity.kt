package com.example.logistica.com.example.logistica

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.logistica.data.remote.ApiService
import com.example.logistica.data.remote.RetrofitClient
import com.example.logistica.data.remote.TokenManager
import com.example.logistica.data.repository.DeliveryRepository
import com.example.logistica.navigation.AppNavigation
import com.example.logistica.notificaciones.NotificationHelper
import com.example.logistica.ui.theme.Blanco
import com.example.logistica.ui.theme.Cafe
import com.example.logistica.ui.theme.CafeOscuro
import com.example.logistica.ui.theme.Fondo

class MainActivity : ComponentActivity() {

    // Objetos para la conexión con la API
    private lateinit var tokenManager: TokenManager
    private lateinit var apiService: ApiService
    private lateinit var repository: DeliveryRepository

    // Lanzador para pedir el permiso de notificaciones (Android 13+), como en la guía
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(
                this,
                "No recibirás avisos de entregas nuevas. Puedes activarlos en Ajustes.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Barras del sistema transparentes con íconos oscuros (edge-to-edge)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        // 1. Crear el canal de notificaciones
        NotificationHelper(this)

        // 2. Pedir el permiso de notificaciones
        checkNotificationPermission()

        // Inicializar conexión con el backend
        tokenManager = TokenManager(applicationContext)
        apiService = RetrofitClient.crearApi(tokenManager)
        repository = DeliveryRepository(apiService, tokenManager)

        // Interfaz de la aplicación
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = CafeOscuro,
                    secondary = Cafe,
                    background = Fondo,
                    surface = Blanco
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Fondo
                ) {
                    AppNavigation(
                        repository = repository,
                        abrirMapa = { direccion ->
                            abrirGoogleMaps(direccion)
                        }
                    )
                }
            }
        }
    }

    // El permiso POST_NOTIFICATIONS solo existe desde Android 13 (API 33)
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Abrir Google Maps con la dirección del pedido
    private fun abrirGoogleMaps(direccion: String) {
        val consulta = Uri.encode(direccion)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.google.com/maps/search/?api=1&query=$consulta")
        )
        startActivity(intent)
    }
}