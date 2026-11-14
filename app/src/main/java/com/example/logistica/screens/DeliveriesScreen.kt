package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

// Convierte el estado de la API al texto que ve el chofer
fun textoEstado(estado: String): String = when (estado) {
    "pendiente" -> "Pendiente"
    "en_camino" -> "En camino"
    "entregada" -> "Entregada"
    "incidencia" -> "Incidencia"
    else -> estado
}

@Composable
fun DeliveriesScreen(
    nombre: String?,
    entregas: List<Delivery>,
    cargando: Boolean,
    error: String?,
    onSincronizar: () -> Unit,
    onOpen: (Int) -> Unit,
    onLogout: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(18.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        if (nombre.isNullOrBlank()) "Hola" else "Hola, $nombre",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Texto
                    )

                    Text(
                        "${entregas.size} entregas pendientes",
                        fontSize = 11.sp,
                        color = TextoSecundario
                    )
                }

                IconButton(onClick = onLogout) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Cerrar sesión",
                        tint = Cafe
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            if (error != null) {
                InfoBanner(
                    "No se pudieron cargar las entregas",
                    error,
                    colorFondo = FondoNaranja,
                    colorTexto = Naranja
                )
                Spacer(Modifier.height(12.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Ruta del día",
                    fontWeight = FontWeight.SemiBold,
                    color = Texto
                )

                Spacer(Modifier.weight(1f))

                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Cafe
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            if (!cargando && error == null && entregas.isEmpty()) {
                Text(
                    "No tienes entregas pendientes. Toca Sincronizar para revisar de nuevo.",
                    color = TextoSecundario,
                    fontSize = 13.sp
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(entregas, key = { it.id }) { entrega ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpen(entrega.id) },
                        shape = RoundedCornerShape(17.dp),
                        colors = CardDefaults.cardColors(containerColor = Blanco),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {

                        Column(modifier = Modifier.padding(14.dp)) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Pedido #${entrega.pedido.id}",
                                    color = TextoSecundario,
                                    fontSize = 10.sp
                                )

                                Spacer(Modifier.weight(1f))

                                StatusLabel(textoEstado(entrega.estado))
                            }

                            Spacer(Modifier.height(7.dp))

                            Text(
                                entrega.pedido.cliente,
                                color = Texto,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )

                            Row(
                                modifier = Modifier.padding(top = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    null,
                                    tint = TextoSecundario,
                                    modifier = Modifier.size(14.dp)
                                )

                                Spacer(Modifier.width(4.dp))

                                Text(
                                    entrega.pedido.direccion,
                                    color = TextoSecundario,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    null,
                                    tint = Cafe,
                                    modifier = Modifier.size(14.dp)
                                )

                                Text(
                                    " " + entrega.pedido.productos.joinToString {
                                        "${it.cantidad} × ${it.producto}"
                                    },
                                    color = Cafe,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    "Ver detalle  ›",
                                    color = CafeOscuro,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        NavigationBar(containerColor = Blanco) {

            NavigationBarItem(
                selected = true,
                onClick = {},
                icon = { Icon(Icons.Default.LocalShipping, null) },
                label = { Text("Entregas") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Cafe,
                    selectedTextColor = Cafe,
                    indicatorColor = Fondo
                )
            )

            NavigationBarItem(
                selected = false,
                onClick = onSincronizar,
                icon = { Icon(Icons.Default.Sync, null) },
                label = { Text("Sincronizar") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Cafe
                )
            )
        }
    }
}