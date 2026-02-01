package com.example.vibracion_morse.ventanas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
    irChat: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val chats by viewModel.misChats.collectAsState()
    var chatSeleccionado by remember { mutableStateOf<Int?>(null) }
    val CelestePrincipal = Color(0xFF4DD0E1)

    LaunchedEffect(usuarioLogueado) {
        viewModel.cargarChats(usuarioLogueado)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tus conversaciones",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelestePrincipal
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.mostrarDialogo = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir Chat", tint = CelestePrincipal)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (chats.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No tienes conversaciones.", color = Color.Gray)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(chats) { chat ->
                            val esSeleccionado = chatSeleccionado == chat.id
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (esSeleccionado) CelestePrincipal.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                                ),
                                onClick = {
                                    if (esSeleccionado) {
                                        irChat(chat.usuarioContacto)
                                    } else {
                                        chatSeleccionado = chat.id
                                        val morse = textoAMorse(chat.usuarioContacto)
                                        vibrarPatronMorse(context, morse)
                                    }
                                }
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(text = chat.usuarioContacto, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                    if (esSeleccionado) Text("Pulsa otra vez para entrar", style = MaterialTheme.typography.bodySmall, color = CelestePrincipal)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { irTraductorManual() },
                    modifier = Modifier
                        .weight(0.7f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("TRADUCTOR", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { irAjustes() },
                    modifier = Modifier
                        .weight(0.3f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = Color.Black)
                }
            }
        }

        if (viewModel.mostrarDialogo) {
            var nuevoContacto by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { viewModel.mostrarDialogo = false },
                title = { Text("Nuevo Chat") },
                text = {
                    Column {
                        Text("Escribe el nombre de un usuario para iniciar un chat con él")
                        OutlinedTextField(value = nuevoContacto, onValueChange = { nuevoContacto = it; viewModel.errorDialogo = null }, label = { Text("Usuario") })
                        if (viewModel.errorDialogo != null) Text(viewModel.errorDialogo!!, color = Color.Red)
                    }
                },
                confirmButton = {
                    Button(onClick = { viewModel.intentarCrearChat(usuarioLogueado, nuevoContacto) }) { Text("AÑADIR") }
                }
            )
        }
    }
}