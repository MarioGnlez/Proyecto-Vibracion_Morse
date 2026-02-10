package com.example.vibracion_morse.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibracion_morse.datos.AppDatabase
import com.example.vibracion_morse.datos.Seguimiento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SeguimientoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).seguimientoDao()

    private val _registros = MutableStateFlow<List<Seguimiento>>(emptyList())
    val registros = _registros.asStateFlow()

    var fechaSeleccionada by mutableStateOf("")
    var nombreProfesional by mutableStateOf("")
    var nuevaNota by mutableStateOf("")

    init {
        fechaSeleccionada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    fun cargarRegistros(pacienteId: String) {
        viewModelScope.launch {
            dao.obtenerSeguimientoPorPaciente(pacienteId).collect { lista ->
                _registros.value = lista
            }
        }
    }

    fun agregarRegistro(pacienteId: String) {
        if (nuevaNota.isBlank() || fechaSeleccionada.isBlank() || nombreProfesional.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            val registro = Seguimiento(
                pacienteId = pacienteId,
                empleadoNombre = nombreProfesional,
                fecha = fechaSeleccionada,
                nota = nuevaNota
            )
            dao.insertarRegistro(registro)

            nuevaNota = ""
            nombreProfesional = ""
            fechaSeleccionada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        }
    }

    fun actualizarFecha(millis: Long?) {
        millis?.let {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            fechaSeleccionada = formatter.format(Date(it))
        }
    }
}