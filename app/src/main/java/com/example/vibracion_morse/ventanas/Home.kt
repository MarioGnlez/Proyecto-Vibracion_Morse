package com.example.vibracion_morse.ventanas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vibracion_morse.textoAMorse
import com.example.vibracion_morse.vibrarPatronMorse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    usuarioLogueado: String,
    irTraductorManual: () -> Unit,
    irAjustes: () -> Unit,
    irChat: (String) -> Unit, // Pasaremos el nombre del contacto
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val chats by viewModel.misChats.collectAsState()

    // Estado local para saber qué tarjeta está seleccionada actualmente
    var chatSeleccionado by remember { mutableStateOf<Int?>(null) }

    // Colores tema
    val CelestePrincipal = Color(0xFF4DD0E1)

    // Cargar chats al iniciar
    LaunchedEffect(usuarioLogueado) {
        viewModel.cargarChats(usuarioLogueado)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Hola, $usuarioLogueado", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Tus conversaciones", fontSize = 14.sp, color = Color.Gray)
                    }
                },
                actions = {
                    // Botón Añadir Chat (+)
                    IconButton(onClick = { viewModel.mostrarDialogo = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir Chat", tint = CelestePrincipal)
                    }
                    // Botón Ajustes
                    IconButton(onClick = { irAjustes() }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = Color.Gray)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { irTraductorManual() },
                containerColor = CelestePrincipal,
                contentColor = Color.White
            ) {
                Text("Traductor Manual")
            }
        }
    ) { paddingValues ->

        // --- CONTENIDO PRINCIPAL ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (chats.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes chats. Pulsa + para empezar.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(chats) { chat ->
                        val esSeleccionado = chatSeleccionado == chat.id

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (esSeleccionado) CelestePrincipal.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            onClick = {
                                if (esSeleccionado) {
                                    // SEGUNDO CLICK: Abrir chat
                                    irChat(chat.usuarioContacto)
                                } else {
                                    // PRIMER CLICK: Vibrar nombre y seleccionar
                                    chatSeleccionado = chat.id
                                    val morse = textoAMorse(chat.usuarioContacto)
                                    vibrarPatronMorse(context, morse)
                                }
                            }
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = chat.usuarioContacto, // Nombre del contacto
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                if (esSeleccionado) {
                                    Text(
                                        text = "Pulsa otra vez para entrar",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CelestePrincipal,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- POPUP AÑADIR CHAT ---
        if (viewModel.mostrarDialogo) {
            var nuevoContacto by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { viewModel.mostrarDialogo = false },
                title = { Text("Nuevo Chat") },
                text = {
                    Column {
                        Text("Introduce el nombre de usuario exacto:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = nuevoContacto,
                            onValueChange = {
                                nuevoContacto = it
                                viewModel.errorDialogo = null
                            },
                            singleLine = true,
                            label = { Text("Usuario") }
                        )
                        if (viewModel.errorDialogo != null) {
                            Text(viewModel.errorDialogo!!, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.intentarCrearChat(usuarioLogueado, nuevoContacto) },
                        colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal)
                    ) {
                        Text("AÑADIR", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.mostrarDialogo = false }) {
                        Text("CANCELAR")
                    }
                }
            )
        }
    }
}