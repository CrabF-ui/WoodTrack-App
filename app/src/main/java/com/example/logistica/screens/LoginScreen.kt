package com.example.logistica.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logistica.components.PrimaryButton
import com.example.logistica.ui.theme.*
import androidx.compose.ui.graphics.Color

@Composable
fun LoginScreen(
    onLogin: () -> Unit
) {
    var usuario by remember { mutableStateOf("carlos.ramirez") }
    var password by remember { mutableStateOf("123456") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .padding(20.dp)
    ) {

        Spacer(Modifier.height(25.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(CafeOscuro, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "TM",
                    color = Blanco,
                    fontWeight = FontWeight.Bold
                )
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

        Spacer(Modifier.height(35.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    Color(0xFFE8D9C5),
                    RoundedCornerShape(18.dp)
                ),
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
            "Ingresa con tus datos. Determinaremos automáticamente si eres chofer o encargado de inventario.",
            color = TextoSecundario,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(25.dp))

        Text("Usuario", fontWeight = FontWeight.SemiBold)

        OutlinedTextField(
            value = usuario,
            onValueChange = {
                usuario = it
                error = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Person, null)
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Cafe,
                unfocusedBorderColor = Blanco,
                focusedContainerColor = Blanco,
                unfocusedContainerColor = Blanco
            )
        )

        Spacer(Modifier.height(15.dp))

        Text("Contraseña", fontWeight = FontWeight.SemiBold)

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(Icons.Default.Lock, null)
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Cafe,
                unfocusedBorderColor = Blanco,
                focusedContainerColor = Blanco,
                unfocusedContainerColor = Blanco
            )
        )

        if (error) {
            Text(
                "Completa todos los campos.",
                color = Rojo,
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(18.dp))

        PrimaryButton("Ingresar") {

            if (usuario.isNotBlank() && password.isNotBlank()) {
                onLogin()
            } else {
                error = true
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