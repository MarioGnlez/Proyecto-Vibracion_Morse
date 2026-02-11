package com.example.vibracion_morse.ventanas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    irSeguimiento: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val listaUsuarios by viewModel.listaUsuarios.collectAsState()
    val CelestePrincipal = Color(0xFF4DD0E1)

    LaunchedEffect(usuarioLogueado) {
        viewModel.inicializar(usuarioLogueado)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = when (viewModel.rolActual) {
                                "ADMIN" -> "Panel Administrador"
                                "MEDICO" -> "Panel Médico"
                                else -> "Mi Perfil"
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = CelestePrincipal
                        )
                        Text(
                            text = if (viewModel.rolActual == "ADMIN") {
                                if (viewModel.viendoMedicos) "Gestionando Médicos" else "Gestionando Pacientes"
                            } else if (viewModel.rolActual == "MEDICO") {
                                "Gestionando Pacientes"
                            } else {
                                "Herramientas de Comunicación"
                            },
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                },
                actions = {
                    // BOTÓN AÑADIR: Visible para ADMIN y MÉDICO
                    if (viewModel.rolActual == "ADMIN" || viewModel.rolActual == "MEDICO") {
                        IconButton(onClick = { viewModel.mostrarDialogoAlta = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo Usuario", tint = CelestePrincipal)
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

            // Selector de vista SOLO para ADMIN
            if (viewModel.rolActual == "ADMIN") {
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Button(
                        onClick = { if (!viewModel.viendoMedicos) viewModel.alternarVista() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (viewModel.viendoMedicos) CelestePrincipal else Color.LightGray
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("MÉDICOS")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { if (viewModel.viendoMedicos) viewModel.alternarVista() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!viewModel.viendoMedicos) CelestePrincipal else Color.LightGray
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("PACIENTES")
                    }
                }
            }

            // Lista de usuarios (ADMIN y MÉDICO ven listas, PACIENTE no ve lista de usuarios)
            if (viewModel.rolActual != "PACIENTE") {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                    items(listaUsuarios) { usuario ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = CelestePrincipal)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(text = usuario.nombreCompleto, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        Text(text = "ID: ${usuario.usuario} | Rol: ${usuario.rol}", color = Color.Gray, fontSize = 14.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    // Botón Historial (Solo para pacientes)
                                    if (usuario.rol == "PACIENTE") {
                                        Button(
                                            onClick = { irSeguimiento(usuario.usuario) },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726))
                                        ) {
                                            Icon(Icons.Default.Info, contentDescription = "Seguimiento")
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("HISTORIAL")
                                        }
                                    }

                                    // Botón Borrar: ADMIN borra todo, MEDICO borra solo pacientes
                                    val puedeBorrar = viewModel.rolActual == "ADMIN" ||
                                            (viewModel.rolActual == "MEDICO" && usuario.rol == "PACIENTE")

                                    if (puedeBorrar) {
                                        Button(
                                            onClick = { viewModel.borrarUsuario(usuario.usuario) },
                                            modifier = Modifier.weight(if(usuario.rol == "PACIENTE") 0.5f else 1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Borrar")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Si es PACIENTE, ocupa el espacio vacío para empujar los botones abajo
                Spacer(modifier = Modifier.weight(1f))
            }

            // Botones inferiores SOLO para PACIENTES (Herramientas de uso)
            if (viewModel.rolActual == "PACIENTE") {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { irTraductorManual() },
                        modifier = Modifier.weight(0.7f).height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("HERRAMIENTA TRADUCTOR", color = Color.White, fontWeight = FontWeight.Bold)
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

        // Diálogo de Alta (Para ADMIN y MÉDICO)
        if (viewModel.mostrarDialogoAlta) {
            var nombre by remember { mutableStateOf("") }
            var usuario by remember { mutableStateOf("") }
            var pass by remember { mutableStateOf("") }
            var tlf by remember { mutableStateOf("") }
            val CelestePrincipal = Color(0xFF4DD0E1)

            // Si es ADMIN y ve médicos -> Crea Médico
            // Si es ADMIN y ve pacientes -> Crea Paciente
            // Si es MÉDICO -> Siempre crea Paciente (viewModel.viendoMedicos es false)
            val tipoEntidad = if (viewModel.viendoMedicos && viewModel.rolActual == "ADMIN") "Médico" else "Paciente"

            AlertDialog(
                onDismissRequest = { viewModel.mostrarDialogoAlta = false },
                containerColor = Color.White,
                title = { Text("Alta Nuevo $tipoEntidad", color = CelestePrincipal, fontWeight = FontWeight.Bold) },
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
                        onClick = { viewModel.crearUsuario(nombre, usuario, pass, tlf) },
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