package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.logistica.components.*
import com.example.logistica.ui.theme.*

@Composable
fun DeliveryDetailScreen(
    onBack: () -> Unit,
    onMap: () -> Unit,
    onUpdate: () -> Unit,
    onIncident: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(18.dp)
    ) {

        ScreenHeader(
            "Detalle de entrega",
            "PED-4821",
            onBack
        )

        Card(
            shape = RoundedCornerShape(17.dp),
            colors = CardDefaults.cardColors(
                containerColor = Blanco
            )
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
                        .background(
                            FondoNaranja,
                            RoundedCornerShape(12.dp)
                        ),
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
                    StatusLabel("Pendiente")

                    Spacer(Modifier.height(5.dp))

                    Text(
                        "Credenza nogal 180 cm",
                        color = Texto,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )

                    Text(
                        "1 pieza · 86 kg",
                        color = TextoSecundario,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        DetailCard(
            "CLIENTE",
            "María González · 55 3124 8892",
            Icons.Default.Person
        )

        DetailCard(
            "DIRECCIÓN",
            "Av. Las Torres 184, Col. Centro, Guadalajara, Jalisco",
            Icons.Default.LocationOn
        )

        DetailCard(
            "NOTAS",
            "Entregar por acceso lateral. Llamar 10 minutos antes. Requiere dos personas.",
            Icons.Default.Description
        )

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            "Abrir en Google Maps",
            Icons.Default.Navigation,
            onMap
        )

        Spacer(Modifier.height(10.dp))

        SecondaryButton(
            "Actualizar estado",
            onUpdate
        )

        TextButton(
            onClick = onIncident,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Registrar incidencia",
                color = Rojo
            )
        }

        Text(
            "Verifica los productos antes de marcar la entrega.",
            color = Naranja,
            fontSize = 10.sp
        )
    }
}