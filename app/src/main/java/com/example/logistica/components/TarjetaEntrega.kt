package com.example.logistica.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.data.model.Delivery
import com.example.logistica.screens.textoEstado
import com.example.logistica.ui.theme.*

// Tarjeta de una entrega. Se usa en la lista de entregas y en "Finalizadas".
// - siguiente: muestra la etiqueta verde "Siguiente entrega"
// - finalizadaEl: texto con día y hora en que se finalizó (vista "Finalizadas")
// - onClick: si es null, la tarjeta es solo de lectura (sin "Ver detalle")
@Composable
fun TarjetaEntrega(
    entrega: Delivery,
    siguiente: Boolean = false,
    finalizadaEl: String? = null,
    onClick: (() -> Unit)? = null
) {
    TarjetaWood(
        modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
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

            if (siguiente) {
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

            // Día y hora en que se finalizó (solo en "Finalizadas")
            if (finalizadaEl != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Schedule,
                        null,
                        tint = TextoSecundario,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(Modifier.width(6.dp))

                    Text(
                        finalizadaEl,
                        color = Texto,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.height(6.dp))
            }

            // Productos (+ "Ver detalle" si se puede tocar)
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
                    color = if (finalizadaEl != null) TextoSecundario else Texto,
                    fontSize = 13.sp,
                    fontWeight = if (finalizadaEl != null) FontWeight.Normal else FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (onClick != null) {
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