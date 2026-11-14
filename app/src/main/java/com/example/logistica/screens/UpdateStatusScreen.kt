package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.logistica.components.*
import com.example.logistica.ui.theme.*

@Composable
fun UpdateStatusScreen(
    estadoActual: String,
    onBack: () -> Unit,
    onSave: (String) -> Unit,
    onIncident: () -> Unit
) {

    var estado by remember {
        mutableStateOf(estadoActual)
    }

    val opciones = listOf(
        "Pendiente" to "Aún no inicia la ruta",
        "En camino" to "Vas hacia el domicilio",
        "Entregada" to "Producto recibido por el cliente"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        ScreenHeader(
            "Actualizar estado",
            "PED-4821 · María González",
            onBack
        )

        Text(
            "¿Cuál es el estado actual?",
            color = Texto,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            "El cliente y el equipo de planta podrán ver esta actualización.",
            color = TextoSecundario,
            fontSize = 11.sp
        )

        Spacer(Modifier.height(18.dp))

        opciones.forEach { opcion ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 9.dp),
                shape = RoundedCornerShape(13.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Blanco
                ),
                border = if (estado == opcion.first)
                    androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        Cafe
                    )
                else null
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.LocalShipping,
                        null,
                        tint = Cafe
                    )

                    Spacer(Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            opcion.first,
                            color = Texto,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            opcion.second,
                            color = TextoSecundario,
                            fontSize = 10.sp
                        )
                    }

                    RadioButton(
                        selected = estado == opcion.first,
                        onClick = {
                            estado = opcion.first
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Cafe
                        )
                    )
                }
            }
        }

        TextButton(
            onClick = onIncident,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Warning, null, tint = Naranja)
            Spacer(Modifier.width(8.dp))
            Text(
                "Registrar incidencia",
                color = Naranja
            )
        }

        Spacer(Modifier.weight(1f))

        InfoBanner(
            "Sincronización pendiente",
            "Se enviará al servidor al recuperar conexión",
            FondoNaranja,
            Naranja
        )

        Spacer(Modifier.height(15.dp))

        PrimaryButton(
            "Guardar estado",
            Icons.Default.Check
        ) {
            onSave(estado)
        }
    }
}