package com.example.vibracion_morse.ventanas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibracion_morse.ConfiguracionVibracion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAjustes(
    irHome: () -> Unit
) {
    val CelestePrincipal = Color(0xFF4DD0E1)

    var letrasMs by remember { mutableFloatStateOf(ConfiguracionVibracion.esperaEntreLetras.toFloat()) }
    var palabrasMs by remember { mutableFloatStateOf(ConfiguracionVibracion.esperaEntrePalabras.toFloat()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Configuración",
                        fontWeight = FontWeight.Bold,
                        color = CelestePrincipal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = irHome) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Ajustes de Vibración",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Espacio entre letras: ${letrasMs.toLong()} ms",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Slider(
                            value = letrasMs,
                            onValueChange = {
                                letrasMs = it
                                ConfiguracionVibracion.esperaEntreLetras = it.toLong()
                            },
                            valueRange = 100f..1000f,
                            colors = SliderDefaults.colors(
                                thumbColor = CelestePrincipal,
                                activeTrackColor = CelestePrincipal,
                                inactiveTrackColor = CelestePrincipal.copy(alpha = 0.2f)
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Espacio entre palabras: ${palabrasMs.toLong()} ms",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Slider(
                            value = palabrasMs,
                            onValueChange = {
                                palabrasMs = it
                                ConfiguracionVibracion.esperaEntrePalabras = it.toLong()
                            },
                            valueRange = 300f..2000f,
                            colors = SliderDefaults.colors(
                                thumbColor = CelestePrincipal,
                                activeTrackColor = CelestePrincipal,
                                inactiveTrackColor = CelestePrincipal.copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            }

            Button(
                onClick = irHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("GUARDAR Y VOLVER", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}