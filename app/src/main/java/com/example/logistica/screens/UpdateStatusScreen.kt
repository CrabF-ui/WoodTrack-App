package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.components.*
import com.example.logistica.data.model.Delivery
import com.example.logistica.ui.theme.*

@Composable
fun UpdateStatusScreen(
    entrega: Delivery?,
    guardando: Boolean,
    error: String?,
    onBack: () -> Unit,
    onSave: (String) -> Unit,
    onIncident: () -> Unit
) {

    // La API solo permite cambiar entre estos dos
    val opciones = listOf(
        Triple("pendiente", "Pendiente", "Aún no inicias la ruta"),
        Triple("en_camino", "En camino", "Vas hacia el domicilio")
    )

    var estado by remember(entrega?.id) {
        mutableStateOf(entrega?.estado ?: "pendiente")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        if (entrega == null) {
            Column(Modifier.padding(18.dp)) {
                ScreenHeader("Actualizar estado", "", onBack)
                Text(
                    "No se encontró la entrega. Regresa y toca Sincronizar.",
                    color = TextoSecundario,
                    fontSize = 13.sp
                )
            }
            return@Column
        }

        // ---------- PARTE DE ARRIBA: opciones con scroll ----------
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 18.dp, end = 18.dp, top = 14.dp)
        ) {

            ScreenHeader(
                "Actualizar estado",
                "Pedido #${entrega.pedido.id} · ${entrega.pedido.cliente}",
                onBack
            )

            Text(
                "¿Cuál es el estado actual?",
                color = Texto,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Actualiza el estado para informar al equipo de planta.",
                color = TextoSecundario,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(16.dp))

            // Pendiente / En camino
            opciones.forEach { (valor, titulo, descripcion) ->

                val seleccionada = estado == valor

                TarjetaWood(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .clickable { estado = valor },
                    seleccionada = seleccionada
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconoCuadro(
                            if (valor == "pendiente") Icons.Outlined.Schedule
                            else Icons.Outlined.LocalShipping
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(Modifier.weight(1f)) {
                            Text(titulo, color = Texto, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            Text(descripcion, color = TextoSecundario, fontSize = 12.sp)
                        }

                        RadioButton(
                            selected = seleccionada,
                            onClick = { estado = valor },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Cafe,
                                unselectedColor = Borde
                            )
                        )
                    }
                }
            }

            // Registrar incidencia: lleva a su propia pantalla
            TarjetaWood(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .clickable { onIncident() }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconoCuadro(
                        Icons.Outlined.Warning,
                        fondo = FondoRojo,
                        color = Rojo
                    )

                    Spacer(Modifier.width(12.dp))

                    Column(Modifier.weight(1f)) {
                        Text("Registrar incidencia", color = Texto, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        Text("Hubo un problema con la entrega", color = TextoSecundario, fontSize = 12.sp)
                    }

                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            InfoBanner(
                "¿Ya entregaste?",
                "Para marcarla como entregada, regresa al detalle y usa Registrar entrega."
            )

            if (error != null) {
                Spacer(Modifier.height(10.dp))
                InfoBanner(
                    "No se pudo guardar",
                    error,
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
                if (guardando) "Guardando..." else "Guardar estado",
                Icons.Outlined.Check
            ) {
                if (!guardando) {
                    onSave(estado)
                }
            }

            Text(
                "Podrás cambiarlo de nuevo si te equivocas.",
                color = TextoSecundario,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}