package com.example.logistica.screens

import android.content.ActivityNotFoundException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.components.*
import com.example.logistica.data.model.Delivery
import com.example.logistica.ui.theme.*
import com.example.logistica.util.*
import java.io.File

@Composable
fun DeliverScreen(
    entrega: Delivery?,
    guardando: Boolean,
    error: String?,
    onBack: () -> Unit,
    onSave: (receptor: String, fotos: List<File>, firma: File?, motivoSinFirma: String?) -> Unit
) {
    val context = LocalContext.current

    // rememberSaveable: si Android cierra la app al abrir la cámara
    // (celulares de gama baja), no se pierden el nombre ni las fotos
    var receptor by rememberSaveable { mutableStateOf("") }
    var fotos by rememberSaveable { mutableStateOf(arrayListOf<String>()) }
    var fotoPendiente by rememberSaveable { mutableStateOf<String?>(null) }
    var sinFirma by rememberSaveable { mutableStateOf(false) }
    var motivoSinFirma by rememberSaveable { mutableStateOf("") }

    val trazos = remember { mutableStateListOf<List<Offset>>() }
    var trazoActual by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var tamanoFirma by remember { mutableStateOf(IntSize.Zero) }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    // Abre la cámara y guarda la foto en el archivo que le damos
    val camara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { tomada ->
        val ruta = fotoPendiente
        if (tomada && ruta != null) {
            comprimirFoto(ruta)
            fotos = ArrayList(fotos + ruta)
            errorLocal = null
        }
        fotoPendiente = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {

        if (entrega == null) {
            ScreenHeader("Registrar entrega", "", onBack)
            Text(
                "Cargando entrega...",
                color = TextoSecundario,
                fontSize = 13.sp
            )
            return@Column
        }

        ScreenHeader(
            "Registrar entrega",
            "Pedido #${entrega.pedido.id} · ${entrega.pedido.cliente}",
            onBack
        )

        if (entrega.estado != "en_camino") {
            Text(
                "La entrega debe estar En camino para registrarla. Cámbiala desde Actualizar estado.",
                color = TextoSecundario,
                fontSize = 13.sp
            )
            return@Column
        }

        // ---------- QUIÉN RECIBE ----------
        Text("¿Quién recibe?", fontWeight = FontWeight.SemiBold, color = Texto)

        OutlinedTextField(
            value = receptor,
            onValueChange = {
                receptor = it
                errorLocal = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            singleLine = true,
            placeholder = { Text("Nombre de la persona", fontSize = 12.sp) },
            shape = RoundedCornerShape(13.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Cafe,
                unfocusedBorderColor = Blanco,
                focusedContainerColor = Blanco,
                unfocusedContainerColor = Blanco
            )
        )

        Spacer(Modifier.height(18.dp))

        // ---------- FOTOS ----------
        Text("Fotos (1 a 3)", fontWeight = FontWeight.SemiBold, color = Texto)

        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            fotos.forEach { ruta ->
                val imagen = remember(ruta) { miniaturaFoto(ruta)?.asImageBitmap() }

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Blanco)
                ) {
                    if (imagen != null) {
                        Image(
                            imagen,
                            contentDescription = "Foto de evidencia",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    IconButton(
                        onClick = {
                            fotos = ArrayList(fotos - ruta)
                            File(ruta).delete()
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(30.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Quitar foto",
                            tint = Blanco,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .padding(3.dp)
                        )
                    }
                }
            }

            if (fotos.size < 3) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Blanco)
                        .clickable {
                            try {
                                val archivo = crearArchivoFoto(context)
                                fotoPendiente = archivo.path
                                camara.launch(uriParaCamara(context, archivo))
                            } catch (e: ActivityNotFoundException) {
                                errorLocal = "No se encontró una app de cámara."
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CameraAlt, null, tint = Cafe)
                        Text("Tomar foto", color = Cafe, fontSize = 10.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // ---------- FIRMA ----------
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Firma del cliente",
                fontWeight = FontWeight.SemiBold,
                color = Texto,
                modifier = Modifier.weight(1f)
            )

            if (!sinFirma) {
                TextButton(onClick = { trazos.clear() }) {
                    Text("Limpiar", color = Cafe)
                }
            }
        }

        if (!sinFirma) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Blanco)
                    .onSizeChanged { tamanoFirma = it }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { inicio ->
                                trazoActual = listOf(inicio)
                            },
                            onDrag = { cambio, _ ->
                                cambio.consume()
                                trazoActual = trazoActual + cambio.position
                            },
                            onDragEnd = {
                                trazos.add(trazoActual)
                                trazoActual = emptyList()
                                errorLocal = null
                            },
                            onDragCancel = {
                                trazoActual = emptyList()
                            }
                        )
                    }
            ) {
                (trazos + listOf(trazoActual)).forEach { trazo ->
                    for (i in 1 until trazo.size) {
                        drawLine(
                            color = Color.Black,
                            start = trazo[i - 1],
                            end = trazo[i],
                            strokeWidth = 6f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            Text(
                "Pide al cliente que firme con el dedo dentro del recuadro.",
                color = TextoSecundario,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Checkbox(
                checked = sinFirma,
                onCheckedChange = {
                    sinFirma = it
                    errorLocal = null
                },
                colors = CheckboxDefaults.colors(checkedColor = Cafe)
            )
            Text("El cliente no puede firmar", color = Texto, fontSize = 13.sp)
        }

        if (sinFirma) {
            OutlinedTextField(
                value = motivoSinFirma,
                onValueChange = {
                    motivoSinFirma = it
                    errorLocal = null
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Ej. Recibió el vigilante, el cliente no estaba", fontSize = 12.sp)
                },
                minLines = 2,
                shape = RoundedCornerShape(13.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Cafe,
                    unfocusedBorderColor = Blanco,
                    focusedContainerColor = Blanco,
                    unfocusedContainerColor = Blanco
                )
            )
        }

        Spacer(Modifier.height(20.dp))

        // ---------- ERRORES ----------
        val mensajeError = errorLocal ?: error
        if (mensajeError != null) {
            InfoBanner(
                "No se pudo registrar",
                mensajeError,
                FondoNaranja,
                Naranja
            )
            Spacer(Modifier.height(15.dp))
        }

        // ---------- ENVIAR ----------
        PrimaryButton(
            if (guardando) "Enviando..." else "Registrar entrega",
            Icons.Default.CheckCircle
        ) {
            if (guardando) return@PrimaryButton

            val nombre = receptor.trim()

            when {
                nombre.length < 2 ->
                    errorLocal = "Escribe el nombre de quien recibe."
                nombre.length > 120 ->
                    errorLocal = "El nombre es demasiado largo (máximo 120 caracteres)."
                fotos.isEmpty() ->
                    errorLocal = "Toma al menos una foto."
                !sinFirma && trazos.isEmpty() ->
                    errorLocal = "Pide al cliente que firme o marca que no puede firmar."
                sinFirma && motivoSinFirma.trim().length < 10 ->
                    errorLocal = "Explica por qué no firmó (mínimo 10 caracteres)."
                else -> {
                    val firma = if (!sinFirma) {
                        guardarFirma(context, trazos, tamanoFirma.width, tamanoFirma.height)
                    } else {
                        null
                    }

                    onSave(
                        nombre,
                        fotos.map { File(it) },
                        firma,
                        if (sinFirma) motivoSinFirma.trim() else null
                    )
                }
            }
        }
    }
}