package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.components.PrimaryButton
import com.example.logistica.ui.theme.*
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun LoginScreen(
    cargando: Boolean,
    mensajeError: String?,
    onLogin: (String, String) -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verPassword by remember { mutableStateOf(false) }

    val colorescampo = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Cafe,
        unfocusedBorderColor = Blanco,
        focusedContainerColor = Blanco,
        unfocusedContainerColor = Blanco
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()   // no quedar debajo de ◁ ○ □
            .imePadding()              // subir cuando sale el teclado
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Spacer(Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(CafeOscuro, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("TM", color = Blanco, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.width(10.dp))

            Column {
                Text(
                    "Taller Madero",
                    fontWeight = FontWeight.Bold,
                    color = Texto,
                    fontSize = 16.sp
                )
                Text(
                    "Operación de planta y ruta",
                    color = TextoSecundario,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(Modifier.height(25.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFFE8D9C5), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "TALLER MADERO",
                color = Cafe,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "Tu jornada, bien organizada",
            fontSize = 26.sp,
            color = Texto,
            fontWeight = FontWeight.Medium,
            lineHeight = 30.sp
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Ingresa con el correo y la contraseña que te dio tu supervisor.",
            color = TextoSecundario,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(25.dp))

        Text("Correo", fontWeight = FontWeight.SemiBold)

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            singleLine = true,
            placeholder = { Text("chofer@ejemplo.com", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Email, null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(14.dp),
            colors = colorescampo
        )

        Spacer(Modifier.height(15.dp))
//CONTRASEÑA
        Text("Contraseña", fontWeight = FontWeight.SemiBold)

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            singleLine = true,
            visualTransformation = if (verPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { verPassword = !verPassword }) {
                    Icon(
                        if (verPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (verPassword) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            leadingIcon = { Icon(Icons.Default.Lock, null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            // La tecla "Listo" del teclado también inicia sesión
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!cargando) onLogin(correo, password)
                }
            ),
            shape = RoundedCornerShape(14.dp),
            colors = colorescampo
        )

        if (mensajeError != null) {
            Text(
                mensajeError,
                color = Rojo,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        Spacer(Modifier.height(18.dp))

        PrimaryButton(if (cargando) "Ingresando..." else "Ingresar") {
            if (!cargando) {
                onLogin(correo, password)
            }
        }

        Spacer(Modifier.height(15.dp))

        Text(
            "¿No puedes ingresar? Habla con tu supervisor.",
            color = TextoSecundario,
            fontSize = 11.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}