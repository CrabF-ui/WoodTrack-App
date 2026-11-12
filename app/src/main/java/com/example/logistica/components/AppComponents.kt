package com.example.logistica.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.ui.theme.*

@Composable
fun PrimaryButton(
    texto: String,
    icono: ImageVector? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Cafe,
            contentColor = Blanco
        )
    ) {
        if (icono != null) {
            Icon(icono, contentDescription = null)
            Spacer(Modifier.width(8.dp))
        }

        Text(
            texto,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SecondaryButton(
    texto: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = CafeOscuro
        )
    ) {
        Text(texto, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ScreenHeader(
    titulo: String,
    subtitulo: String = "",
    onBack: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(38.dp)
                    .background(Blanco, CircleShape)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Texto
                )
            }

            Spacer(Modifier.width(10.dp))
        }

        Column {
            Text(
                titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Texto
            )

            if (subtitulo.isNotBlank()) {
                Text(
                    subtitulo,
                    fontSize = 11.sp,
                    color = TextoSecundario
                )
            }
        }
    }
}

@Composable
fun StatusLabel(estado: String) {

    val (fondo, color) = when (estado) {
        "Entregada" -> FondoVerde to Verde
        "En camino" -> Color(0xFFE5F0F3) to Color(0xFF467789)
        else -> FondoNaranja to Naranja
    }

    Surface(
        color = fondo,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            estado,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
        )
    }
}

@Composable
fun InfoBanner(
    titulo: String,
    descripcion: String,
    colorFondo: Color = FondoVerde,
    colorTexto: Color = Verde
) {
    Surface(
        color = colorFondo,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                titulo,
                color = colorTexto,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            )

            Text(
                descripcion,
                color = colorTexto,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun DetailCard(
    titulo: String,
    descripcion: String,
    icono: ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                icono,
                contentDescription = null,
                tint = Cafe
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    titulo,
                    fontSize = 10.sp,
                    color = TextoSecundario,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    descripcion,
                    fontSize = 12.sp,
                    color = Texto
                )
            }
        }
    }
}