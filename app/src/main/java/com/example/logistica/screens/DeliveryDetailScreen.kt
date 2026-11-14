package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.components.*
import com.example.logistica.data.model.Delivery
import com.example.logistica.ui.theme.*

@Composable
fun DeliveryDetailScreen(
    entrega: Delivery?,
    cargando: Boolean,
    onBack: () -> Unit,
    onMap: (String) -> Unit,
    onUpdate: () -> Unit,
    onIncident: () -> Unit,
    onDeliver: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(18.dp)
    ) {

        // Si la entrega ya no está en la lista (por ejemplo, se cerró)
        if (entrega == null) {
            ScreenHeader("Detalle de entrega", "", onBack)

            Text(
                if (cargando) "Cargando entrega..."
                else "No se encontró la entrega. Regresa y toca Sincronizar.",
                color = TextoSecundario,
                fontSize = 13.sp
            )
            return@Column
        }

        val pedido = entrega.pedido
        val totalPiezas = pedido.productos.sumOf { it.cantidad }

        ScreenHeader(
            "Detalle de entrega",
            "Pedido #${pedido.id}",
            onBack
        )

        Card(
            shape = RoundedCornerShape(17.dp),
            colors = CardDefaults.cardColors(containerColor = Blanco)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(65.dp)
                        .background(FondoNaranja, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Home,
                        null,
                        tint = Cafe,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    StatusLabel(textoEstado(entrega.estado))

                    Spacer(Modifier.height(5.dp))

                    Text(
                        pedido.productos.joinToString {
                            "${it.cantidad} × ${it.producto}"
                        },
                        color = Texto,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )

                    Text(
                        "$totalPiezas piezas · Entrega ${entrega.fecha_asignada}",
                        color = TextoSecundario,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        DetailCard(
            "CLIENTE",
            if (pedido.telefono.isNullOrBlank()) pedido.cliente
            else "${pedido.cliente} · ${pedido.telefono}",
            Icons.Default.Person
        )

        DetailCard(
            "DIRECCIÓN",
            pedido.direccion,
            Icons.Default.LocationOn
        )

        DetailCard(
            "NOTAS",
            entrega.notas ?: "Sin notas",
            Icons.Default.Description
        )

        Spacer(Modifier.weight(1f))

        // Solo se puede entregar si está "en camino"
        if (entrega.estado == "en_camino") {
            PrimaryButton(
                "Registrar entrega",
                Icons.Default.CheckCircle,
                onDeliver
            )

            Spacer(Modifier.height(10.dp))
        }
        PrimaryButton(
            "Abrir en Google Maps",
            Icons.Default.Navigation
        ) {
            onMap(pedido.direccion)
        }

        Spacer(Modifier.height(10.dp))

        SecondaryButton(
            "Actualizar estado",
            onUpdate
        )

        TextButton(
            onClick = onIncident,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar incidencia", color = Rojo)
        }

        Text(
            "Verifica los productos antes de marcar la entrega.",
            color = Naranja,
            fontSize = 10.sp
        )
    }
}