package com.example.logistica.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logistica.ui.theme.*

// Barra inferior: Entregas · Finalizadas · Sincronizar
// seleccionado: 0 = Entregas, 1 = Finalizadas
@Composable
fun BottomBar(
    seleccionado: Int,
    onEntregas: () -> Unit,
    onFinalizadas: () -> Unit,
    onSincronizar: () -> Unit
) {
    val colores = NavigationBarItemDefaults.colors(
        selectedIconColor = CafeOscuro,
        selectedTextColor = CafeOscuro,
        indicatorColor = CafeClaro,
        unselectedIconColor = TextoSecundario,
        unselectedTextColor = TextoSecundario
    )

    Column {
        HorizontalDivider(color = Borde)

        NavigationBar(
            containerColor = Blanco,
            tonalElevation = 0.dp
        ) {

            NavigationBarItem(
                selected = seleccionado == 0,
                onClick = onEntregas,
                icon = { Icon(Icons.Outlined.LocalShipping, contentDescription = null) },
                label = {
                    Text(
                        "Entregas",
                        fontWeight = if (seleccionado == 0) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = colores
            )

            NavigationBarItem(
                selected = seleccionado == 1,
                onClick = onFinalizadas,
                icon = { Icon(Icons.Outlined.TaskAlt, contentDescription = null) },
                label = {
                    Text(
                        "Finalizadas",
                        fontWeight = if (seleccionado == 1) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = colores
            )

            // No es una pantalla: recarga la pantalla actual
            NavigationBarItem(
                selected = false,
                onClick = onSincronizar,
                icon = { Icon(Icons.Outlined.Sync, contentDescription = null) },
                label = { Text("Sincronizar") },
                colors = colores
            )
        }
    }
}