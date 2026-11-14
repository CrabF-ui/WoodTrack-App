package com.example.logistica.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.components.*
import com.example.logistica.data.model.Delivery
import com.example.logistica.ui.theme.*

@Composable
fun IncidentScreen(
    entrega: Delivery?,
    guardando: Boolean,
    error: String?,
    onBack: () -> Unit,
    onSave: (String) -> Unit
) {

    val motivos = listOf(
        "Cliente rechazó",
        "Cliente ausente",
        "Producto dañado",
        "Otro"
    )

    var motivo by remember { mutableStateOf("Cliente ausente") }
    var notas by remember { mutableStateOf("") }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {

        if (entrega == null) {
            ScreenHeader("Registrar incidencia", "", onBack)
            Text(
                "No se encontró la entrega. Regresa y toca Sincronizar.",
                color = TextoSecundario,
                fontSize = 13.sp
            )
            return@Column
        }

        ScreenHeader(
            "Registrar incidencia",
            "Pedido #${entrega.pedido.id} · ${entrega.pedido.cliente}",
            onBack
        )

        Text(
            "¿Qué ocurrió?",
            fontSize = 18.sp,
            color = Texto,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            "La entrega se cerrará y planta verá el motivo.",
            fontSize = 11.sp,
            color = TextoSecundario
        )

        Spacer(Modifier.height(18.dp))

        motivos.forEach { opcion ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 9.dp),
                shape = RoundedCornerShape(13.dp),
                colors = CardDefaults.cardColors(containerColor = Blanco),
                border = if (motivo == opcion) BorderStroke(1.5.dp, Cafe) else null
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        when (opcion) {
                            "Producto dañado" -> Icons.Default.Warning
                            "Otro" -> Icons.Default.MoreHoriz
                            else -> Icons.Default.Person
                        },
                        null,
                        tint = Cafe
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        opcion,
                        modifier = Modifier.weight(1f),
                        color = Texto,
                        fontSize = 13.sp
                    )

                    RadioButton(
                        selected = motivo == opcion,
                        onClick = {
                            motivo = opcion
                            errorLocal = null
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = Cafe)
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            if (motivo == "Otro") "Notas (obligatorio)" else "Notas",
            fontWeight = FontWeight.SemiBold,
            color = Texto,
            fontSize = 12.sp
        )

        OutlinedTextField(
            value = notas,
            onValueChange = {
                notas = it
                errorLocal = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            placeholder = {
                Text("Ej. Llamé dos veces y esperé 15 minutos", fontSize = 12.sp)
            },
            minLines = 3,
            shape = RoundedCornerShape(13.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Cafe,
                unfocusedBorderColor = Blanco,
                focusedContainerColor = Blanco,
                unfocusedContainerColor = Blanco
            )
        )

        Spacer(Modifier.height(20.dp))

        val mensajeError = errorLocal ?: error
        if (mensajeError != null) {
            InfoBanner(
                "No se pudo registrar",
                mensajeError,
                FondoNaranja,
                Naranja
            )
            Spacer(Modifier.height(15.dp))
        }

        PrimaryButton(
            if (guardando) "Registrando..." else "Registrar incidencia",
            Icons.Default.Warning
        ) {
            if (guardando) return@PrimaryButton

            // Lo que se manda a la API: motivo + notas
            val texto = if (notas.isBlank()) motivo else "$motivo: ${notas.trim()}"

            when {
                motivo == "Otro" && notas.isBlank() ->
                    errorLocal = "Escribe en las notas qué pasó."
                texto.length < 10 ->
                    errorLocal = "El motivo debe tener al menos 10 caracteres."
                else ->
                    onSave(texto)
            }
        }
    }
}