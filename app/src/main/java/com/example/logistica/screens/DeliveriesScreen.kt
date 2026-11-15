package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.components.*
import com.example.logistica.data.model.Delivery
import com.example.logistica.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Convierte el estado de la API al texto que ve el chofer (lo usan otras pantallas)
fun textoEstado(estado: String): String = when (estado) {
    "pendiente" -> "Pendiente"
    "en_camino" -> "En camino"
    "entregada" -> "Entregada"
    "incidencia" -> "Incidencia"
    else -> estado
}

// "Buenos días", "Buenas tardes" o "Buenas noches" según la hora
private fun saludo(): String {
    val hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hora) {
        in 5..11 -> "Buenos días"
        in 12..18 -> "Buenas tardes"
        else -> "Buenas noches"
    }
}

// "Domingo 15"
private fun fechaHoy(): String {
    val texto = SimpleDateFormat("EEEE d", Locale("es", "MX")).format(Date())
    return texto.replaceFirstChar { it.uppercase() }
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

    var confirmarSalida by remember { mutableStateOf(false) }

    if (confirmarSalida) {
        AlertDialog(
            onDismissRequest = { confirmarSalida = false },
            containerColor = Blanco,
            title = { Text("¿Cerrar sesión?", fontWeight = FontWeight.Bold) },
            text = { Text("Tendrás que volver a ingresar con tu correo y contraseña.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarSalida = false
                    onLogout()
                }) {
                    Text("Cerrar sesión", color = Rojo, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarSalida = false }) {
                    Text("Cancelar", color = CafeOscuro)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 18.dp)
        ) {

            Spacer(Modifier.height(14.dp))

            // ---------- ENCABEZADO ----------
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    // Saludo chico arriba
                    Text(
                        if (nombre.isNullOrBlank()) saludo() else "${saludo()},",
                        fontSize = 14.sp,
                        color = TextoSecundario
                    )

                    // Nombre grande abajo (si es muy largo, baja a una segunda línea)
                    if (!nombre.isNullOrBlank()) {
                        Text(
                            nombre,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Texto,
                            lineHeight = 26.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        "${fechaHoy()} · ${entregas.size} " +
                                if (entregas.size == 1) "entrega asignada" else "entregas asignadas",
                        fontSize = 12.sp,
                        color = TextoSecundario
                    )
                }

                IconButton(onClick = { confirmarSalida = true }) {
                    Icon(
                        Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            if (error != null) {
                InfoBanner(
                    "No se pudieron cargar las entregas",
                    error,
                    colorFondo = FondoNaranja,
                    colorTexto = Naranja,
                    icono = Icons.Outlined.Warning
                )
                Spacer(Modifier.height(14.dp))
            }

            // ---------- TÍTULO DE SECCIÓN ----------
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Entregas de hoy",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Texto
                )

                Spacer(Modifier.weight(1f))

                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = CafeOscuro
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // ---------- LISTA VACÍA ----------
            if (!cargando && error == null && entregas.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconoCuadro(Icons.Outlined.Inventory2, tamano = 72.dp)

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "No hay entregas pendientes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Texto
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "Toca Sincronizar para revisar si te asignaron nuevas.",
                        fontSize = 13.sp,
                        color = TextoSecundario,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ---------- TARJETAS ----------
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 14.dp)
            ) {
                itemsIndexed(entregas, key = { _, entrega -> entrega.id }) { indice, entrega ->

                    TarjetaWood(
                        modifier = Modifier.clickable { onOpen(entrega.id) }
                    ) {

                        Column(modifier = Modifier.padding(14.dp)) {

                            // Pedido + estado
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Pedido #${entrega.pedido.id}",
                                    color = TextoSecundario,
                                    fontSize = 12.sp
                                )

                                Spacer(Modifier.weight(1f))

                                StatusLabel(textoEstado(entrega.estado))
                            }

                            // La primera de la lista es la siguiente según el orden de la API
                            if (indice == 0) {
                                Spacer(Modifier.height(6.dp))
                                Surface(
                                    color = FondoVerde,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        "Siguiente entrega",
                                        color = Verde,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(Modifier.height(6.dp))

                            // Cliente
                            Text(
                                entrega.pedido.cliente,
                                color = Texto,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )

                            // Dirección
                            Row(
                                modifier = Modifier.padding(top = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.LocationOn,
                                    null,
                                    tint = TextoSecundario,
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(Modifier.width(4.dp))

                                Text(
                                    entrega.pedido.direccion,
                                    color = TextoSecundario,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = Borde
                            )

                            // Productos + ver detalle
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.ShoppingCart,
                                    null,
                                    tint = Cafe,
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(Modifier.width(6.dp))

                                Text(
                                    entrega.pedido.productos.joinToString {
                                        "${it.cantidad} × ${it.producto}"
                                    },
                                    color = Texto,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    "Ver detalle  ›",
                                    color = Verde,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // ---------- BARRA DE ABAJO ----------
        HorizontalDivider(color = Borde)

        NavigationBar(
            containerColor = Blanco,
            tonalElevation = 0.dp
        ) {

            NavigationBarItem(
                selected = true,
                onClick = {},
                icon = { Icon(Icons.Outlined.LocalShipping, null) },
                label = { Text("Entregas", fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CafeOscuro,
                    selectedTextColor = CafeOscuro,
                    indicatorColor = Blanco
                )
            )

            NavigationBarItem(
                selected = false,
                onClick = onSincronizar,
                icon = { Icon(Icons.Outlined.Sync, null) },
                label = { Text("Sincronizar") },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = TextoSecundario,
                    unselectedTextColor = TextoSecundario
                )
            )
        }
    }
}