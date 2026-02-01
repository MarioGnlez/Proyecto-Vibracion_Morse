package com.example.vibracion_morse.ventanas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.vibracion_morse.textoAMorse
import com.example.vibracion_morse.vibrarPatronMorse
import com.example.vibracion_morse.datos.Mensaje
import com.example.vibracion_morse.datos.AppDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaChat(
    miUsuario: String,
    otroUsuario: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val mensajeDao = db.mensajeDao()

    val mensajes by mensajeDao.obtenerConversacion(miUsuario, otroUsuario)
        .collectAsState(initial = emptyList())

    var textoAEnviar by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val CelestePrincipal = Color(0xFF4DD0E1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(otroUsuario) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                reverseLayout = true,
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(mensajes.reversed()) { mensaje ->
                    val esMio = mensaje.remitente == miUsuario
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = if (esMio) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Card(
                            onClick = {
                                val textoALeer = "${mensaje.remitente} DIJO ${mensaje.texto}"
                                val morse = textoAMorse(textoALeer)
                                vibrarPatronMorse(context, morse)
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (esMio) CelestePrincipal else Color.LightGray
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            Text(
                                text = mensaje.texto,
                                modifier = Modifier.padding(16.dp),
                                color = if (esMio) Color.White else Color.Black,
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = textoAEnviar,
                    onValueChange = { textoAEnviar = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe un mensaje...") },
                    maxLines = 4,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        if (textoAEnviar.isNotBlank()) {
                            val texto = textoAEnviar
                            textoAEnviar = ""

                            val morse = textoAMorse(texto)
                            vibrarPatronMorse(context, morse)

                            coroutineScope.launch(Dispatchers.IO) {
                                mensajeDao.enviarMensaje(
                                    Mensaje(
                                        remitente = miUsuario,
                                        destinatario = otroUsuario,
                                        texto = texto
                                    )
                                )
                            }
                        }
                    },
                    containerColor = CelestePrincipal,
                    contentColor = Color.White
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
                }
            }
        }
    }
}