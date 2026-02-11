package com.example.vibracion_morse.ventanas

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
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
import com.example.vibracion_morse.datos.Seguimiento
import com.example.vibracion_morse.viewmodels.SeguimientoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaSeguimiento(
    pacienteId: String,
    usuarioAdminLogueado: String,
    irAtras: () -> Unit,
    viewModel: SeguimientoViewModel = viewModel()
) {
    val context = LocalContext.current
    val registros by viewModel.registros.collectAsState()
    val CelestePrincipal = Color(0xFF4DD0E1)

    var mostrarCalendario by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(pacienteId) {
        viewModel.cargarRegistros(pacienteId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial: $pacienteId", fontWeight = FontWeight.Bold, color = CelestePrincipal) },
                navigationIcon = {
                    IconButton(onClick = irAtras) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { generarInformeClinico(context, pacienteId, registros) }) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = "Exportar Informe", tint = CelestePrincipal)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Nuevo Registro Clínico", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = viewModel.fechaSeleccionada,
                        onValueChange = { },
                        label = { Text("Fecha") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { mostrarCalendario = true },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray,
                            disabledLabelColor = Color.Black,
                            disabledTrailingIconColor = CelestePrincipal
                        ),
                        trailingIcon = {
                            IconButton(onClick = { mostrarCalendario = true }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = viewModel.nombreProfesional,
                        onValueChange = { viewModel.nombreProfesional = it },
                        label = { Text("Profesional / Empleado") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CelestePrincipal) },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = viewModel.nuevaNota,
                        onValueChange = { viewModel.nuevaNota = it },
                        label = { Text("Escriba evolución del paciente...") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.agregarRegistro(pacienteId) },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("GUARDAR REGISTRO")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Historial de Seguimiento", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(registros) { reg ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(reg.fecha, fontWeight = FontWeight.Bold, color = CelestePrincipal)
                                Text("Por: ${reg.empleadoNombre}", fontSize = 12.sp, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(reg.nota)
                        }
                    }
                }
            }
        }

        if (mostrarCalendario) {
            DatePickerDialog(
                onDismissRequest = { mostrarCalendario = false },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.actualizarFecha(datePickerState.selectedDateMillis)
                        mostrarCalendario = false
                    }) {
                        Text("ACEPTAR", color = CelestePrincipal)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarCalendario = false }) {
                        Text("CANCELAR", color = Color.Gray)
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

fun generarInformeClinico(context: Context, pacienteId: String, registros: List<Seguimiento>) {
    try {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val nombreArchivo = "Historial_${pacienteId}_$timeStamp.txt"

        val contenido = StringBuilder()
        contenido.append("INFORME CLÍNICO - MORSE CHAT\n")
        contenido.append("====================================\n")
        contenido.append("PACIENTE: $pacienteId\n")
        contenido.append("FECHA EMISIÓN: $timeStamp\n")
        contenido.append("====================================\n\n")

        if (registros.isEmpty()) {
            contenido.append("No existen registros clínicos para este paciente.\n")
        } else {
            registros.forEach { reg ->
                contenido.append("FECHA: ${reg.fecha}\n")
                contenido.append("PROFESIONAL: ${reg.empleadoNombre}\n")
                contenido.append("NOTA: ${reg.nota}\n")
                contenido.append("------------------------------------\n")
            }
        }

        contenido.append("\n--- Fin del Informe ---")

        context.openFileOutput(nombreArchivo, Context.MODE_PRIVATE).use {
            it.write(contenido.toString().toByteArray())
        }

        Toast.makeText(context, "Informe exportado: $nombreArchivo", Toast.LENGTH_LONG).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al generar informe", Toast.LENGTH_SHORT).show()
    }
}