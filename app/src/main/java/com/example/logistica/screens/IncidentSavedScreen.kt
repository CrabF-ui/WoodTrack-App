package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.LocalShipping
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
fun IncidentSavedScreen(
    motivo: String,
    onHome: () -> Unit,
    onDetail: () -> Unit
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
                Icons.Default.CloudUpload,
                null,
                tint = Naranja,
                modifier = Modifier.size(35.dp)
            )
        }

        Spacer(Modifier.height(22.dp))

        Text(
            "Guardado en este equipo",
            color = Texto,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Registramos \"$motivo\". No necesitas repetir el proceso.",
            color = TextoSecundario,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(20.dp))

        InfoBanner(
            "Sincronización pendiente",
            "Se enviará al recuperar la conexión.",
            FondoNaranja,
            Naranja
        )

        Spacer(Modifier.height(18.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Blanco
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(
                Modifier.padding(15.dp)
            ) {

                Row(Modifier.fillMaxWidth()) {
                    Text("Pedido", color = TextoSecundario)
                    Spacer(Modifier.weight(1f))
                    Text("PED-4821", color = Texto)
                }

                Spacer(Modifier.height(12.dp))

                Row(Modifier.fillMaxWidth()) {
                    Text("Motivo", color = TextoSecundario)
                    Spacer(Modifier.weight(1f))
                    Text(motivo, color = Texto)
                }

                Spacer(Modifier.height(12.dp))

                Row(Modifier.fillMaxWidth()) {
                    Text("Hora", color = TextoSecundario)
                    Spacer(Modifier.weight(1f))
                    Text("11:28", color = Texto)
                }
            }
        }

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            "Volver a mis entregas",
            Icons.Default.LocalShipping,
            onHome
        )

        Spacer(Modifier.height(10.dp))

        SecondaryButton(
            "Ver detalle",
            onDetail
        )
    }
}