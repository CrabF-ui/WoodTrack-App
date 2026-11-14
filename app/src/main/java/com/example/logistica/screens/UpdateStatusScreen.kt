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
        Triple("pendiente", "Pendiente", "Aún no inicia la ruta"),
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
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        if (entrega == null) {
            ScreenHeader("Actualizar estado", "", onBack)
            Text(
                "No se encontró la entrega. Regresa y toca Sincronizar.",
                color = TextoSecundario,
                fontSize = 13.sp
            )
            return@Column
        }

        ScreenHeader(
            "Actualizar estado",
            "Pedido #${entrega.pedido.id} · ${entrega.pedido.cliente}",
            onBack
        )

        Text(
            "¿Cuál es el estado actual?",
            color = Texto,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            "Para marcarla como entregada, regresa al detalle y registra la entrega con fotos.",
            color = TextoSecundario,
            fontSize = 11.sp
        )

        Spacer(Modifier.height(18.dp))

        opciones.forEach { (valor, titulo, descripcion) ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 9.dp),
                shape = RoundedCornerShape(13.dp),
                colors = CardDefaults.cardColors(containerColor = Blanco),
                border = if (estado == valor) BorderStroke(1.5.dp, Cafe) else null
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(Icons.Default.LocalShipping, null, tint = Cafe)

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(titulo, color = Texto, fontWeight = FontWeight.SemiBold)
                        Text(descripcion, color = TextoSecundario, fontSize = 10.sp)
                    }

                    RadioButton(
                        selected = estado == valor,
                        onClick = { estado = valor },
                        colors = RadioButtonDefaults.colors(selectedColor = Cafe)
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
            Text("Registrar incidencia", color = Naranja)
        }

        Spacer(Modifier.height(20.dp))

        if (error != null) {
            InfoBanner(
                "No se pudo guardar",
                error,
                FondoNaranja,
                Naranja
            )
            Spacer(Modifier.height(15.dp))
        }

        PrimaryButton(
            if (guardando) "Guardando..." else "Guardar estado",
            Icons.Default.Check
        ) {
            if (!guardando) {
                onSave(estado)
            }
        }
    }
}