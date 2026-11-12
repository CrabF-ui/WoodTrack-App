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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.logistica.components.*
import com.example.logistica.ui.theme.*

data class Entrega(
    val id: Int,
    val pedido: String,
    val cliente: String,
    val direccion: String,
    val producto: String,
    val distancia: String,
    val estado: String
)

val entregasEjemplo = listOf(
    Entrega(
        1,
        "PED-4821",
        "María González",
        "Av. Las Torres 184, Col. Centro",
        "Credenza nogal 180 cm",
        "1.2 km",
        "Pendiente"
    ),
    Entrega(
        2,
        "PED-4822",
        "Constructora Roble",
        "Calle Fresno 28, Parque Industrial",
        "Mesa de comedor",
        "3.8 km",
        "En camino"
    ),
    Entrega(
        3,
        "PED-4823",
        "Javier Soto",
        "Priv. del Lago 16, Jardines",
        "Puerta de madera",
        "6.1 km",
        "Pendiente"
    )
)

@Composable
fun DeliveriesScreen(
    onOpen: (Int) -> Unit,
    onLogout: () -> Unit
) {
    var sincronizando by remember { mutableStateOf(false) }

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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Buenos días, Carlos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Texto
                    )

                    Text(
                        "Martes 26 · 4 entregas asignadas",
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

            InfoBanner(
                "Trabajando sin conexión",
                "Tus cambios se guardarán en este equipo"
            )

            Spacer(Modifier.height(17.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Ruta sugerida",
                    fontWeight = FontWeight.SemiBold,
                    color = Texto
                )

                Spacer(Modifier.weight(1f))

                Text(
                    "1 de 4 completadas",
                    color = Verde,
                    fontSize = 11.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(entregasEjemplo) { entrega ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOpen(entrega.id)
                            },
                        shape = RoundedCornerShape(17.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Blanco
                        ),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    entrega.pedido,
                                    color = TextoSecundario,
                                    fontSize = 10.sp
                                )

                                Spacer(Modifier.weight(1f))

                                StatusLabel(entrega.estado)
                            }

                            Spacer(Modifier.height(7.dp))

                            Text(
                                entrega.cliente,
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
                                    entrega.direccion,
                                    color = TextoSecundario,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Place,
                                    null,
                                    tint = Cafe,
                                    modifier = Modifier.size(14.dp)
                                )

                                Text(
                                    " ${entrega.distancia} · siguiente sugerida",
                                    color = Cafe,
                                    fontSize = 10.sp
                                )

                                Spacer(Modifier.weight(1f))

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

        NavigationBar(
            containerColor = Blanco
        ) {

            NavigationBarItem(
                selected = true,
                onClick = {},
                icon = {
                    Icon(Icons.Default.LocalShipping, null)
                },
                label = {
                    Text("Entregas")
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Cafe,
                    selectedTextColor = Cafe,
                    indicatorColor = Fondo
                )
            )

            NavigationBarItem(
                selected = false,
                onClick = {
                    sincronizando = true
                },
                icon = {
                    Icon(Icons.Default.Sync, null)
                },
                label = {
                    Text("Sincronizar")
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Cafe
                )
            )
        }
    }
}