package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.components.*
import com.example.logistica.ui.theme.*

@Composable
fun IncidentSavedScreen(
    motivo: String,
    pedido: String,
    hora: String,
    onHome: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {

        Spacer(Modifier.height(65.dp))

        Box(
            modifier = Modifier
                .size(76.dp)
                .align(Alignment.CenterHorizontally)
                .background(FondoNaranja, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.CheckCircle,
                null,
                tint = Naranja,
                modifier = Modifier.size(35.dp)
            )
        }

        Spacer(Modifier.height(22.dp))

        Text(
            "Incidencia registrada",
            color = Texto,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Planta ya puede ver el motivo. La entrega quedó cerrada.",
            color = TextoSecundario,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Blanco),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(Modifier.padding(15.dp)) {

                Row(Modifier.fillMaxWidth()) {
                    Text("Pedido", color = TextoSecundario)
                    Spacer(Modifier.weight(1f))
                    Text(pedido, color = Texto)
                }

                Spacer(Modifier.height(12.dp))

                Row(Modifier.fillMaxWidth()) {
                    Text("Motivo", color = TextoSecundario)
                    Spacer(Modifier.width(16.dp))
                    Text(motivo, color = Texto, modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.height(12.dp))

                Row(Modifier.fillMaxWidth()) {
                    Text("Hora", color = TextoSecundario)
                    Spacer(Modifier.weight(1f))
                    Text(hora, color = Texto)
                }
            }
        }

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            "Volver a mis entregas",
            Icons.Default.LocalShipping,
            onHome
        )
    }
}