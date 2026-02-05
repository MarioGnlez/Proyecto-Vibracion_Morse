package com.example.vibracion_morse.ventanas

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.vibracion_morse.textoAMorse
import com.example.vibracion_morse.vibrarPatronMorse
import com.example.vibracion_morse.datos.Mensaje
import com.example.vibracion_morse.datos.AppDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    // Cargamos los mensajes en tiempo real desde la base de datos
    val mensajes by mensajeDao.obtenerConversacion(miUsuario, otroUsuario)
        .collectAsState(initial = emptyList())

    var textoAEnviar by remember { mutableStateOf("") }
    // Variable para controlar si el menú de los 3 puntos está abierto
    var mostrarMenu by remember { mutableStateOf(false) }

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
                },
                actions = {
                    // Botón de opciones (3 puntos) arriba a la derecha
                    IconButton(onClick = { mostrarMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                    }
                    // Menú desplegable con la opción de exportar
                    DropdownMenu(
                        expanded = mostrarMenu,
                        onDismissRequest = { mostrarMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Exportar Informe (.txt)") },
                            onClick = {
                                mostrarMenu = false
                                // Llamamos a la función que crea el archivo de texto
                                generarInformeChat(context, miUsuario, otroUsuario, mensajes)
                            }
                        )
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
            // Lista de mensajes. "reverseLayout" hace que empiece desde abajo
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
                            .padding(vertical = 6.dp),
                        contentAlignment = if (esMio) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Card(
                            onClick = {
                                // Al tocar el mensaje, vibramos el contenido en Morse
                                val textoALeer = "${mensaje.remitente} DIJO ${mensaje.texto}"
                                val morse = textoAMorse(textoALeer)
                                vibrarPatronMorse(context, morse)
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (esMio) CelestePrincipal else Color.LightGray
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = mensaje.texto,
                                    color = if (esMio) Color.White else Color.Black,
                                    fontSize = 22.sp,
                                    lineHeight = 30.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = mensaje.fecha,
                                    color = if (esMio) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f),
                                    fontSize = 14.sp,
                                    modifier = Modifier.align(Alignment.End),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }
            }

            // Barra inferior para escribir
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
                    placeholder = { Text("Escribe un mensaje...", fontSize = 18.sp) },
                    maxLines = 4,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        if (textoAEnviar.isNotBlank()) {
                            val texto = textoAEnviar
                            textoAEnviar = ""

                            // Vibramos lo que acabas de enviar
                            val morse = textoAMorse(texto)
                            vibrarPatronMorse(context, morse)

                            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            val fechaActual = sdf.format(Date())

                            // Guardamos el mensaje en la base de datos en segundo plano
                            coroutineScope.launch(Dispatchers.IO) {
                                mensajeDao.enviarMensaje(
                                    Mensaje(
                                        remitente = miUsuario,
                                        destinatario = otroUsuario,
                                        texto = texto,
                                        fecha = fechaActual
                                    )
                                )
                            }
                        }
                    },
                    containerColor = CelestePrincipal,
                    contentColor = Color.White,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

// Función que crea el archivo .txt con todo el historial
fun generarInformeChat(
    context: android.content.Context,
    usuario1: String,
    usuario2: String,
    mensajes: List<Mensaje>
) {
    try {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val nombreArchivo = "Chat_${usuario1}_${usuario2}_$timeStamp.txt"

        val contenido = StringBuilder()
        contenido.append("INFORME DE CONVERSACIÓN - MORSE CHAT\n")
        contenido.append("------------------------------------\n")
        contenido.append("Fecha de generación: $timeStamp\n")
        contenido.append("Participantes: $usuario1 y $usuario2\n")
        contenido.append("------------------------------------\n\n")

        mensajes.forEach { m ->
            contenido.append("[${m.fecha}] ${m.remitente}: ${m.texto}\n")
        }

        contenido.append("\n--- Fin del informe ---")

        // Guardamos el archivo en la memoria privada de la app
        context.openFileOutput(nombreArchivo, android.content.Context.MODE_PRIVATE).use {
            it.write(contenido.toString().toByteArray())
        }

        Toast.makeText(context, "Informe guardado: $nombreArchivo", Toast.LENGTH_LONG).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al generar informe", Toast.LENGTH_SHORT).show()
    }
}