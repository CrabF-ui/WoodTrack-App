package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.components.*
import com.example.logistica.ui.theme.*
import com.example.logistica.util.Fechas
import com.example.logistica.viewmodel.FinalizadasUiState
import com.example.logistica.viewmodel.Periodo
import com.example.logistica.viewmodel.TipoFinalizada

// Vista "Finalizadas": entregas entregadas o con incidencia. Solo lectura.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishedDeliveriesScreen(
    estado: FinalizadasUiState,
    onPeriodo: (Periodo) -> Unit,
    onFecha: (Long) -> Unit,          // milisegundos que regresa el calendario
    onTipo: (TipoFinalizada) -> Unit,
    onReintentar: () -> Unit,
    onEntregas: () -> Unit
) {
    var mostrarCalendario by remember { mutableStateOf(false) }
    val lista = estado.filtradas()

    // ---------- CALENDARIO ----------
    if (mostrarCalendario) {
        val calendario = rememberDatePickerState(
            initialSelectedDateMillis = Fechas.aDatePicker(
                estado.fechaElegida ?: System.currentTimeMillis()
            )
        )

        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    calendario.selectedDateMillis?.let { onFecha(it) }
                    mostrarCalendario = false
                }) {
                    Text("Aceptar", color = CafeOscuro, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) {
                    Text("Cancelar", color = TextoSecundario)
                }
            },
            colors = DatePickerDefaults.colors(containerColor = Blanco)
        ) {
            DatePicker(
                state = calendario,
                colors = DatePickerDefaults.colors(
                    containerColor = Blanco,
                    selectedDayContainerColor = CafeOscuro,
                    todayDateBorderColor = Cafe,
                    todayContentColor = Cafe
                )
            )
        }
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
            Text(
                "Finalizadas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Texto
            )

            Text(
                estado.textoPeriodo(),
                fontSize = 12.sp,
                color = TextoSecundario
            )

            Spacer(Modifier.height(14.dp))

            // ---------- FILTRO: PERIODO ----------
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipFiltro("Hoy", estado.periodo == Periodo.HOY) {
                    onPeriodo(Periodo.HOY)
                }
                ChipFiltro("Semana", estado.periodo == Periodo.SEMANA) {
                    onPeriodo(Periodo.SEMANA)
                }
                ChipFiltro(
                    "Fecha",
                    estado.periodo == Periodo.FECHA,
                    Icons.Outlined.CalendarMonth
                ) {
                    mostrarCalendario = true
                }
            }

            Spacer(Modifier.height(6.dp))

            // ---------- FILTRO: TIPO ----------
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipFiltro("Todas", estado.tipo == TipoFinalizada.TODAS) {
                    onTipo(TipoFinalizada.TODAS)
                }
                ChipFiltro("Entregadas", estado.tipo == TipoFinalizada.ENTREGADAS) {
                    onTipo(TipoFinalizada.ENTREGADAS)
                }
                ChipFiltro("Incidencias", estado.tipo == TipoFinalizada.INCIDENCIAS) {
                    onTipo(TipoFinalizada.INCIDENCIAS)
                }
            }

            Spacer(Modifier.height(14.dp))

            // ---------- CONTENIDO ----------
            when {
                // Cargando
                estado.cargando -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = CafeOscuro)
                    }
                }

                // Error (sin conexión, etc.)
                estado.error != null -> {
                    InfoBanner(
                        "No se pudieron cargar las finalizadas",
                        estado.error,
                        colorFondo = FondoNaranja,
                        colorTexto = Naranja,
                        icono = Icons.Outlined.Warning
                    )

                    Spacer(Modifier.height(12.dp))

                    SecondaryButton(
                        "Reintentar",
                        onReintentar,
                        Icons.Outlined.Refresh
                    )
                }

                // Vacío
                lista.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconoCuadro(Icons.Outlined.TaskAlt, tamano = 72.dp)

                        Spacer(Modifier.height(16.dp))

                        Text(
                            estado.textoVacio(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Texto,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Lista
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 14.dp)
                    ) {
                        items(lista, key = { it.id }) { entrega ->
                            // Solo lectura: sin onClick
                            TarjetaEntrega(
                                entrega = entrega,
                                finalizadaEl = Fechas.formatoDiaHora(entrega.estado_fecha_hora)
                            )
                        }
                    }
                }
            }
        }

        // ---------- BARRA DE ABAJO ----------
        BottomBar(
            seleccionado = 1,
            onEntregas = onEntregas,
            onFinalizadas = {},
            onSincronizar = onReintentar   // aquí "Sincronizar" recarga las finalizadas
        )
    }
}

// Chip de filtro: café oscuro cuando está elegido, blanco con borde cuando no
@Composable
private fun ChipFiltro(
    texto: String,
    seleccionado: Boolean,
    icono: ImageVector? = null,
    onClick: () -> Unit
) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = {
            Text(
                texto,
                fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
            )
        },
        leadingIcon = if (icono != null) {
            { Icon(icono, contentDescription = null, modifier = Modifier.size(16.dp)) }
        } else {
            null
        },
        shape = RoundedCornerShape(10.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Blanco,
            labelColor = Texto,
            iconColor = TextoSecundario,
            selectedContainerColor = CafeOscuro,
            selectedLabelColor = Blanco,
            selectedLeadingIconColor = Blanco
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = seleccionado,
            borderColor = Borde,
            selectedBorderColor = CafeOscuro
        )
    )
}