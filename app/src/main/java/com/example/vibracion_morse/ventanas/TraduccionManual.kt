package com.example.vibracion_morse.ventanas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibracion_morse.textoAMorse
import com.example.vibracion_morse.vibrarPatronMorse


@Composable
fun TraduccionManual(
    irHome: () -> Unit
) {
    val context = LocalContext.current
    var textoUsuario by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- BARRA SUPERIOR (Botón volver y Título) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Esto empuja el botón de volver hacia abajo
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Traductor Manual",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = textoUsuario,
                onValueChange = { textoUsuario = it },
                label = { Text("Escribe tu mensaje aquí") },
                placeholder = { Text("Ej: SOS") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Acción (Transmitir)
            Button(
                onClick = {
                    val codigoMorse = textoAMorse(textoUsuario)
                    vibrarPatronMorse(context, codigoMorse)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("TRANSMITIR VIBRACIÓN")
            }

            if (textoUsuario.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Traducción: ${textoAMorse(textoUsuario)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Button(
            onClick = { irHome() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("VOLVER")
        }
    }
}