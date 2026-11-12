package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun IncidentScreen(
    onBack: () -> Unit,
    onSave: (String, String) -> Unit
) {

    val motivos = listOf(
        "Cliente rechazó",
        "Cliente ausente",
        "Producto dañado",
        "Otro"
    )

    var motivo by remember {
        mutableStateOf("Cliente ausente")
    }

    var notas by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(18.dp)
    ) {

        ScreenHeader(
            "Registrar incidencia",
            "PED-4821",
            onBack
        )

        Text(
            "¿Qué ocurrió?",
            fontSize = 18.sp,
            color = Texto,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            "Selecciona un motivo para informar a planta.",
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
                colors = CardDefaults.cardColors(
                    containerColor = Blanco
                ),
                border = if (motivo == opcion)
                    androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        Cafe
                    )
                else null
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {

                    Icon(
                        when (opcion) {
                            "Cliente rechazó" -> Icons.Default.Person
                            "Cliente ausente" -> Icons.Default.Person
                            "Producto dañado" -> Icons.Default.Warning
                            else -> Icons.Default.MoreHoriz
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
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Cafe
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            "Notas",
            fontWeight = FontWeight.SemiBold,
            color = Texto,
            fontSize = 12.sp
        )

        OutlinedTextField(
            value = notas,
            onValueChange = {
                notas = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            placeholder = {
                Text(
                    "Ej. Llamé dos veces y esperé 15 minutos",
                    fontSize = 12.sp
                )
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

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            "Registrar incidencia",
            Icons.Default.Warning
        ) {
            onSave(motivo, notas)
        }
    }
}