package com.example.vibracion_morse.ventanas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vibracion_morse.textoAMorse
import com.example.vibracion_morse.vibrarPatronMorse

@Composable
fun TraduccionManual(
    irHome: () -> Unit
) {
    val context = LocalContext.current
    var textoUsuario by remember { mutableStateOf("") }
    val CelestePrincipal = Color(0xFF4DD0E1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Traductor Manual",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = CelestePrincipal
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Caja grande para escribir lo que quieras
            OutlinedTextField(
                value = textoUsuario,
                onValueChange = { textoUsuario = it },
                label = { Text("Escribe tu mensaje aquí") },
                placeholder = { Text("Ej: SOS") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CelestePrincipal,
                    focusedLabelColor = CelestePrincipal
                ),
                maxLines = 10
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón mágico: convierte el texto a Morse y hace vibrar el móvil
            Button(
                onClick = {
                    val codigoMorse = textoAMorse(textoUsuario)
                    vibrarPatronMorse(context, codigoMorse)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("TRANSMITIR VIBRACIÓN", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Si has escrito algo, te enseño la traducción en puntos y rayas abajo
            if (textoUsuario.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Traducción:", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = textoAMorse(textoUsuario),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Botón para volver al menú principal
        Button(
            onClick = { irHome() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("VOLVER", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}