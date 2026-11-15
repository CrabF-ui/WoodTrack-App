package com.example.logistica.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.ui.theme.*

// ---------- BOTONES ----------

// Botón principal: café oscuro, esquinas suaves
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
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CafeOscuro,
            contentColor = Blanco
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
    ) {
        if (icono != null) {
            Icon(icono, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }

        Text(texto, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}

// Botón secundario: blanco con borde fino (el ícono es opcional)
@Composable
fun SecondaryButton(
    texto: String,
    onClick: () -> Unit,
    icono: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Cafe.copy(alpha = 0.45f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Blanco,
            contentColor = CafeOscuro
        )
    ) {
        if (icono != null) {
            Icon(icono, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }

        Text(texto, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}

// ---------- ENCABEZADO ----------

// Flecha de regresar + título grande + subtítulo chico
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
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Texto
                )
            }

            Spacer(Modifier.width(6.dp))
        }

        Column {
            Text(
                titulo,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Texto
            )

            if (subtitulo.isNotBlank()) {
                Text(
                    subtitulo,
                    fontSize = 12.sp,
                    color = TextoSecundario
                )
            }
        }
    }
}

// ---------- ETIQUETA DE ESTADO ----------

@Composable
fun StatusLabel(estado: String) {

    val (fondo, color) = when (estado) {
        "En camino" -> FondoAzul to Azul
        "Entregada" -> FondoVerde to Verde
        "Incidencia" -> FondoRojo to Rojo
        else -> FondoNaranja to Naranja          // Pendiente
    }

    Surface(
        color = fondo,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            estado,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

// ---------- AVISO ----------

// Ícono + título + descripción (por defecto azul de información)
@Composable
fun InfoBanner(
    titulo: String,
    descripcion: String,
    colorFondo: Color = FondoAzul,
    colorTexto: Color = Azul,
    icono: ImageVector = Icons.Default.Info
) {
    Surface(
        color = colorFondo,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                icono,
                contentDescription = null,
                tint = colorTexto,
                modifier = Modifier.size(18.dp)
            )

            Spacer(Modifier.width(10.dp))

            Column {
                Text(
                    titulo,
                    color = colorTexto,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )

                Text(
                    descripcion,
                    color = colorTexto,
                    fontSize = 11.sp
                )
            }
        }
    }
}

// ---------- TARJETA DE DATO (cliente, dirección, notas) ----------

@Composable
fun DetailCard(
    titulo: String,
    descripcion: String,
    icono: ImageVector
) {
    TarjetaWood(modifier = Modifier.padding(vertical = 5.dp)) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
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
                    descripcion,
                    fontSize = 13.sp,
                    color = Texto
                )
            }
        }
    }
}

// ---------- HERRAMIENTAS NUEVAS (para las siguientes tarjetas) ----------

// Cuadrito de color suave con un ícono adentro
@Composable
fun IconoCuadro(
    icono: ImageVector,
    fondo: Color = FondoIcono,
    color: Color = IconoSuave,
    tamano: Dp = 36.dp
) {
    Box(
        modifier = Modifier
            .size(tamano)
            .background(fondo, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icono,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(tamano * 0.55f)
        )
    }
}

// Tarjeta blanca con borde fino. Si está seleccionada, se pinta café claro.
@Composable
fun TarjetaWood(
    modifier: Modifier = Modifier,
    seleccionada: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionada) CafeClaro else Blanco
        ),
        border = BorderStroke(
            if (seleccionada) 1.5.dp else 1.dp,
            if (seleccionada) Cafe else Borde
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        content = content
    )
}

// Colores de los campos de texto: gris claro con borde fino
@Composable
fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Cafe,
    unfocusedBorderColor = Borde,
    focusedContainerColor = Blanco,
    unfocusedContainerColor = CampoFondo,
    cursorColor = Cafe
)