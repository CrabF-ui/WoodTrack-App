
package com.example.logistica.com.example.logistica

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import com.example.logistica.R
import com.example.logistica.ui.theme.CafeOscuro
import com.example.logistica.ui.theme.Fondo
import kotlinx.coroutines.delay

@Composable
fun PantallaCarga(
    onCargaTerminada: () -> Unit
) {

    LaunchedEffect(Unit) {
        delay(2500)
        onCargaTerminada()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.pantalla_carga
            ),
            contentDescription = "Logo de WoodTrack",
            modifier = Modifier.size(250.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "WoodTrack",
            color = CafeOscuro,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        CircularProgressIndicator(
            color = CafeOscuro
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Cargando aplicación...",
            color = CafeOscuro,
            fontSize = 14.sp
        )
    }
}
