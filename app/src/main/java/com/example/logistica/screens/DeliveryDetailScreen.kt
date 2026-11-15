package com.example.logistica.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
            .navigationBarsPadding()   // no quedar debajo de ◁ ○ □
    ) {

        // Si la entrega ya no está en la lista
        if (entrega == null) {
            Column(Modifier.padding(18.dp)) {
                ScreenHeader("Detalle de entrega", "", onBack)
                Text(
                    if (cargando) "Cargando entrega..."
                    else "No se encontró la entrega. Regresa y toca Sincronizar.",
                    color = TextoSecundario,
                    fontSize = 13.sp
                )
            }
            return@Column
        }

        val pedido = entrega.pedido
        val totalPiezas = pedido.productos.sumOf { it.cantidad }
        val enCamino = entrega.estado == "en_camino"

        // ---------- PARTE DE ARRIBA: información con scroll ----------
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 18.dp, end = 18.dp, top = 14.dp)
        ) {

            ScreenHeader(
                "Detalle de entrega",
                "Pedido #${pedido.id}",
                onBack
            )

            // Productos
            TarjetaWood {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconoCuadro(
                        Icons.Outlined.Chair,
                        fondo = CafeClaro,
                        color = Cafe,
                        tamano = 64.dp
                    )

                    Spacer(Modifier.width(14.dp))

                    Column {
                        StatusLabel(textoEstado(entrega.estado))

                        Spacer(Modifier.height(6.dp))

                        Text(
                            pedido.productos.joinToString {
                                "${it.cantidad} × ${it.producto}"
                            },
                            color = Texto,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            if (totalPiezas == 1) "1 pieza" else "$totalPiezas piezas",
                            color = TextoSecundario,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Cliente, dirección y notas en una sola tarjeta
            TarjetaWood {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FilaDato(
                        "CLIENTE",
                        if (pedido.telefono.isNullOrBlank()) pedido.cliente
                        else "${pedido.cliente} · ${pedido.telefono}",
                        Icons.Outlined.Person
                    )

                    FilaDato(
                        "DIRECCIÓN",
                        pedido.direccion,
                        Icons.Outlined.LocationOn
                    )

                    FilaDato(
                        "NOTAS",
                        entrega.notas ?: "Sin notas",
                        Icons.Outlined.Description
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            InfoBanner(
                "Antes de entregar",
                "Verifica el producto antes de marcar la entrega.",
                colorFondo = FondoNaranja,
                colorTexto = Naranja,
                icono = Icons.Outlined.Shield
            )

            Spacer(Modifier.height(14.dp))
        }

        // ---------- PARTE DE ABAJO: botones fijos ----------
        HorizontalDivider(color = Borde)

        Column(
            modifier = Modifier
                .background(Fondo)
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {

            if (enCamino) {
                // En camino: lo principal es registrar la entrega
                PrimaryButton(
                    "Registrar entrega",
                    Icons.Outlined.CheckCircle,
                    onDeliver
                )

                Spacer(Modifier.height(8.dp))

                // Maps y Actualizar estado lado a lado
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BotonChico(
                        "Maps",
                        Icons.Outlined.Navigation,
                        Modifier.weight(1f)
                    ) {
                        onMap(pedido.direccion)
                    }

                    BotonChico(
                        "Actualizar estado",
                        Icons.Outlined.Sync,
                        Modifier.weight(1f),
                        onUpdate
                    )
                }
            } else {
                // Pendiente: igual que en Figma
                SecondaryButton(
                    "Abrir en Google Maps",
                    { onMap(pedido.direccion) },
                    Icons.Outlined.Navigation
                )

                Spacer(Modifier.height(8.dp))

                PrimaryButton(
                    "Actualizar estado",
                    Icons.Outlined.Sync,
                    onUpdate
                )
            }

            TextButton(
                onClick = onIncident,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Registrar incidencia",
                    color = Rojo,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// Una fila de dato: ícono en cuadrito + título chico + texto
@Composable
private fun FilaDato(
    titulo: String,
    texto: String,
    icono: ImageVector
) {
    Row(verticalAlignment = Alignment.Top) {
        IconoCuadro(icono)

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                titulo,
                fontSize = 10.sp,
                color = TextoSecundario,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(2.dp))

            Text(
                texto,
                fontSize = 14.sp,
                color = Texto
            )
        }
    }
}

// Botón blanco con borde, más chico, para poner dos lado a lado
@Composable
private fun BotonChico(
    texto: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Cafe.copy(alpha = 0.45f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Blanco,
            contentColor = CafeOscuro
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Icon(icono, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(texto, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
    }
}