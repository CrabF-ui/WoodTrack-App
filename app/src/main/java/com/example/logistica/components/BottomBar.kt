package com.example.logistica.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.logistica.ui.theme.*

@Composable
fun BottomBar(
    seleccionado: Int,
    onSeleccionar: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Blanco,
        contentColor = Cafe
    ) {

        NavigationBarItem(
            selected = seleccionado == 0,
            onClick = {
                onSeleccionar(0)
            },
            icon = {
                Icon(
                    Icons.Default.LocalShipping,
                    contentDescription = "Entregas"
                )
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
            selected = seleccionado == 1,
            onClick = {
                onSeleccionar(1)
            },
            icon = {
                Icon(
                    Icons.Default.Sync,
                    contentDescription = "Sincronizar"
                )
            },
            label = {
                Text("Sincronizar")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Cafe,
                selectedTextColor = Cafe,
                indicatorColor = Fondo
            )
        )
    }
}