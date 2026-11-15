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
fun DeliverySavedScreen(
    pedido: String,
    receptor: String,
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

        Spacer(Modifier.height(40.dp))

        // Palomita verde grande
        Box(Modifier.align(Alignment.CenterHorizontally)) {
            IconoCuadro(
                Icons.Outlined.CheckCircle,
                fondo = FondoVerde,
                color = Verde,
                tamano = 96.dp
            )
        }

        Spacer(Modifier.height(22.dp))

        Text(
            "Entrega registrada",
            color = Texto,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(6.dp))

        Text(
            "La evidencia se envió correctamente.",
            color = TextoSecundario,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        TarjetaWood {
            Column(Modifier.padding(16.dp)) {

                FilaConfirmacion("Pedido", pedido)

                HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Borde)

                FilaConfirmacion("Recibió", receptor)

                HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Borde)

                FilaConfirmacion("Hora", hora)
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
private fun FilaConfirmacion(etiqueta: String, valor: String) {
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