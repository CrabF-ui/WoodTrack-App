package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.logistica.components.*
import com.example.logistica.ui.theme.*

@Composable
fun EmptyDeliveriesScreen(
    onRetry: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(18.dp)
    ) {

        ScreenHeader(
            "Mis entregas",
            "Martes 26"
        )

        InfoBanner(
            "Trabajando sin conexión",
            "Tus cambios se guardarán en este equipo."
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    FondoNaranja,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Inventory,
                null,
                tint = Cafe,
                modifier = Modifier.size(45.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "No hay entregas pendientes",
            color = Texto,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Completa tu ruta guardada. No pudimos revisar si hay nuevas asignaciones porque no tienes conexión.",
            color = TextoSecundario,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(15.dp))

        InfoBanner(
            "No se pudo actualizar la ruta",
            "Intenta de nuevo cuando tengas señal.",
            FondoRojo,
            Rojo
        )

        Spacer(Modifier.weight(1f))

        SecondaryButton(
            "Volver a intentar",
            onRetry
        )
    }
}