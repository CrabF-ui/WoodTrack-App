package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HighlightOff
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material.icons.outlined.Warning
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
        "Cliente rechazó" to Icons.Outlined.HighlightOff,
        "Cliente ausente" to Icons.Outlined.PersonOff,
        "Producto dañado" to Icons.Outlined.Inventory2,
        "Otro" to Icons.Outlined.MoreHoriz
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
            .imePadding()              // el botón sube con el teclado
    ) {

        if (entrega == null) {
            Column(Modifier.padding(18.dp)) {
                ScreenHeader("Registrar incidencia", "", onBack)
                Text(
                    "No se encontró la entrega. Regresa y toca Sincronizar.",
                    color = TextoSecundario,
                    fontSize = 13.sp
                )
            }
            return@Column
        }

        // ---------- PARTE DE ARRIBA: motivos y notas con scroll ----------
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 18.dp, end = 18.dp, top = 14.dp)
        ) {

            ScreenHeader(
                "Registrar incidencia",
                "Pedido #${entrega.pedido.id} · ${entrega.pedido.cliente}",
                onBack
            )

            Text(
                "¿Qué ocurrió?",
                fontSize = 20.sp,
                color = Texto,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Selecciona un motivo para informar a planta. La entrega se cerrará.",
                fontSize = 13.sp,
                color = TextoSecundario
            )

            Spacer(Modifier.height(16.dp))

            motivos.forEach { (opcion, icono) ->

                val seleccionada = motivo == opcion

                TarjetaWood(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .clickable {
                            motivo = opcion
                            errorLocal = null
                        },
                    seleccionada = seleccionada
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconoCuadro(icono)

                        Spacer(Modifier.width(12.dp))

                        Text(
                            opcion,
                            modifier = Modifier.weight(1f),
                            color = Texto,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )

                        RadioButton(
                            selected = seleccionada,
                            onClick = {
                                motivo = opcion
                                errorLocal = null
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Cafe,
                                unselectedColor = Borde
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                if (motivo == "Otro") "Notas (obligatorio)" else "Notas",
                fontWeight = FontWeight.SemiBold,
                color = Texto,
                fontSize = 14.sp
            )

            OutlinedTextField(
                value = notas,
                onValueChange = {
                    notas = it
                    errorLocal = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                placeholder = {
                    Text("Ej. Llamé dos veces y esperé 15 minutos", fontSize = 13.sp)
                },
                minLines = 3,
                shape = RoundedCornerShape(10.dp),
                colors = coloresCampo()
            )

            val mensajeError = errorLocal ?: error
            if (mensajeError != null) {
                Spacer(Modifier.height(12.dp))
                InfoBanner(
                    "No se pudo registrar",
                    mensajeError,
                    colorFondo = FondoNaranja,
                    colorTexto = Naranja,
                    icono = Icons.Outlined.Warning
                )
            }

            Spacer(Modifier.height(14.dp))
        }

        // ---------- PARTE DE ABAJO: botón fijo ----------
        HorizontalDivider(color = Borde)

        Column(
            modifier = Modifier
                .background(Fondo)
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            PrimaryButton(
                if (guardando) "Registrando..." else "Registrar incidencia",
                Icons.Outlined.Warning
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
}