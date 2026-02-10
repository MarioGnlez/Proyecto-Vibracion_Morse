package com.example.vibracion_morse.ventanas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vibracion_morse.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    usuarioLogueado: String,
    irTraductorManual: () -> Unit,
    irAjustes: () -> Unit,
    irChat: (String) -> Unit,
    irSeguimiento: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val chats by viewModel.misChats.collectAsState()
    val pacientes by viewModel.listaPacientes.collectAsState()

    val CelestePrincipal = Color(0xFF4DD0E1)

    LaunchedEffect(usuarioLogueado) {
        viewModel.inicializar(usuarioLogueado)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (viewModel.esUsuarioAdmin) "Gestión Pacientes" else "Mis Conversaciones",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelestePrincipal
                    )
                },
                actions = {
                    if (viewModel.esUsuarioAdmin) {
                        IconButton(onClick = { viewModel.mostrarDialogoAlta = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo Paciente", tint = CelestePrincipal)
                        }
                    } else {
                        IconButton(onClick = { viewModel.mostrarDialogoChat = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Añadir Chat", tint = CelestePrincipal)
                        }
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

            if (viewModel.esUsuarioAdmin) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(pacientes) { paciente ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = paciente.nombreCompleto, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(text = "ID: ${paciente.usuario}", color = Color.Gray, fontSize = 14.sp)

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { viewModel.intentarCrearChat(usuarioLogueado, paciente.usuario); irChat(paciente.usuario) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal)
                                    ) {
                                        Icon(Icons.Default.Email, contentDescription = null)
                                    }

                                    Button(
                                        onClick = { irSeguimiento(paciente.usuario) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726))
                                    ) {
                                        Icon(Icons.Default.Info, contentDescription = "Seguimiento")
                                    }

                                    Button(
                                        onClick = { viewModel.borrarPaciente(paciente.usuario) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Borrar")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Column(modifier = Modifier.weight(1f)) {
                    if (chats.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No tienes conversaciones activas.", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(chats) { chat ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                    onClick = { irChat(chat.usuarioContacto) }
                                ) {
                                    Column(modifier = Modifier.padding(20.dp)) {
                                        Text(text = chat.usuarioContacto, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
                        modifier = Modifier.weight(0.7f).height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("TRADUCTOR MANUAL", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { irAjustes() },
                        modifier = Modifier.weight(0.3f).height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = Color.Black)
                    }
                }
            }
        }

        if (viewModel.mostrarDialogoChat) {
            var nuevoContacto by remember { mutableStateOf("") }
            val CelestePrincipal = Color(0xFF4DD0E1)

            AlertDialog(
                onDismissRequest = { viewModel.mostrarDialogoChat = false },
                containerColor = Color.White,
                title = { Text("Nuevo Chat", color = CelestePrincipal, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = nuevoContacto,
                            onValueChange = { nuevoContacto = it; viewModel.errorDialogoChat = null },
                            label = { Text("Nombre de usuario") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (viewModel.errorDialogoChat != null) {
                            Text(viewModel.errorDialogoChat!!, color = Color.Red, fontSize = 14.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.intentarCrearChat(usuarioLogueado, nuevoContacto) },
                        colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal)
                    ) { Text("AÑADIR") }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.mostrarDialogoChat = false }) { Text("CANCELAR") }
                }
            )
        }

        if (viewModel.mostrarDialogoAlta) {
            var nombre by remember { mutableStateOf("") }
            var usuario by remember { mutableStateOf("") }
            var pass by remember { mutableStateOf("") }
            var tlf by remember { mutableStateOf("") }
            val CelestePrincipal = Color(0xFF4DD0E1)

            AlertDialog(
                onDismissRequest = { viewModel.mostrarDialogoAlta = false },
                containerColor = Color.White,
                title = { Text("Alta Nuevo Paciente", color = CelestePrincipal, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") })
                        OutlinedTextField(value = usuario, onValueChange = { usuario = it }, label = { Text("ID Usuario") })
                        OutlinedTextField(
                            value = pass,
                            onValueChange = { pass = it },
                            label = { Text("Contraseña") },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        OutlinedTextField(value = tlf, onValueChange = { tlf = it }, label = { Text("Teléfono") })

                        if (viewModel.errorDialogoAlta != null) {
                            Text(viewModel.errorDialogoAlta!!, color = Color.Red, fontSize = 14.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.crearPaciente(nombre, usuario, pass, tlf) },
                        colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal)
                    ) { Text("REGISTRAR") }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.mostrarDialogoAlta = false }) { Text("CANCELAR") }
                }
            )
        }
    }
}