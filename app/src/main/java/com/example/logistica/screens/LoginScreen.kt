package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.components.PrimaryButton
import com.example.logistica.components.coloresCampo
import com.example.logistica.ui.theme.*

@Composable
fun LoginScreen(
    cargando: Boolean,
    mensajeError: String?,
    onLogin: (String, String) -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
            .statusBarsPadding()
            .navigationBarsPadding()   // no quedar debajo de ◁ ○ □
            .imePadding()              // subir cuando sale el teclado
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {

        Spacer(Modifier.height(64.dp))

        // ---------- ENCABEZADO ----------
        Text(
            "WoodTrack",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Texto,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(6.dp))

        Text(
            "Acceso para choferes",
            fontSize = 15.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(44.dp))

        // ---------- CORREO ----------
        Text(
            "Correo electrónico",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Texto
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Outlined.Person, null, tint = TextoSecundario)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(10.dp),
            colors = coloresCampo()
        )

        Spacer(Modifier.height(18.dp))

        // ---------- CONTRASEÑA ----------
        Text(
            "Contraseña",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Texto
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            singleLine = true,
            visualTransformation = if (verPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            leadingIcon = {
                Icon(Icons.Outlined.Lock, null, tint = TextoSecundario)
            },
            trailingIcon = {
                IconButton(onClick = { verPassword = !verPassword }) {
                    Icon(
                        if (verPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (verPassword) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = TextoSecundario
                    )
                }
            },
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
            shape = RoundedCornerShape(10.dp),
            colors = coloresCampo()
        )

        if (mensajeError != null) {
            Text(
                mensajeError,
                color = Rojo,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        // ---------- BOTÓN ----------
        PrimaryButton(if (cargando) "Ingresando..." else "Ingresar") {
            if (!cargando) {
                onLogin(correo, password)
            }
        }

        Spacer(Modifier.height(22.dp))

        Text(
            "¿Necesitas ayuda? Contacta a tu supervisor.",
            color = TextoSecundario,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
    }
}