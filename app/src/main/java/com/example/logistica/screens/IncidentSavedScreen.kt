package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Spacer(Modifier.height(30.dp))

        Box(Modifier.align(Alignment.CenterHorizontally)) {
            IconoCuadro(
                Icons.Outlined.CheckCircle,
                fondo = FondoNaranja,
                color = Naranja,
                tamano = 76.dp
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "Incidencia registrada",
            color = Texto,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(6.dp))

        Text(
            "Planta ya puede ver el motivo. La entrega quedó cerrada.",
            color = TextoSecundario,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(22.dp))

        TarjetaWood {
            Column(Modifier.padding(16.dp)) {

                FilaResumen("Pedido", pedido)

                HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Borde)

                FilaResumen("Motivo", motivo)

                HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Borde)

                FilaResumen("Hora", hora)
            }
        }

        Spacer(Modifier.height(24.dp))

        PrimaryButton(
            "Volver a mis entregas",
            Icons.Outlined.LocalShipping,
            onHome
        )
    }
}

// Etiqueta a la izquierda y valor a la derecha
@Composable
private fun FilaResumen(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth()) {
        Text(etiqueta, color = TextoSecundario, fontSize = 13.sp)
        Spacer(Modifier.width(16.dp))
        Text(
            valor,
            color = Texto,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}