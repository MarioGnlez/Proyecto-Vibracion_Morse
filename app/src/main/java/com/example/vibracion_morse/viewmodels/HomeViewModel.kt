package com.example.vibracion_morse.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibracion_morse.datos.AppDatabase
import com.example.vibracion_morse.datos.Usuario
import kotlinx.coroutines.CoroutineDispatcher // <--- IMPORTANTE
import kotlinx.coroutines.Dispatchers        // <--- IMPORTANTE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()

    // Variable para pruebas: Por defecto usa IO, pero los tests pueden cambiarla
    var ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val _listaUsuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val listaUsuarios = _listaUsuarios.asStateFlow()

    var rolActual by mutableStateOf("")
    var viendoMedicos by mutableStateOf(false)

    var mostrarDialogoAlta by mutableStateOf(false)
    var errorDialogoAlta by mutableStateOf<String?>(null)

    fun inicializar(miUsuario: String) {
        // Usamos ioDispatcher en lugar de Dispatchers.IO directamente
        viewModelScope.launch(ioDispatcher) {
            val user = usuarioDao.obtenerUsuario(miUsuario)
            rolActual = user?.rol ?: ""

            if (rolActual == "ADMIN") {
                viendoMedicos = true
                cargarMedicos()
            } else {
                viendoMedicos = false
                cargarPacientes()
            }
        }
    }

    fun alternarVista() {
        viendoMedicos = !viendoMedicos
        if (viendoMedicos) cargarMedicos() else cargarPacientes()
    }

    private fun cargarPacientes() {
        viewModelScope.launch(ioDispatcher) { // <--- CAMBIO
            val pacientes = usuarioDao.obtenerUsuariosPorRol("PACIENTE")
            _listaUsuarios.value = pacientes
        }
    }

    private fun cargarMedicos() {
        viewModelScope.launch(ioDispatcher) { // <--- CAMBIO
            val medicos = usuarioDao.obtenerUsuariosPorRol("MEDICO")
            _listaUsuarios.value = medicos
        }
    }

    fun borrarUsuario(nombreUsuario: String) {
        viewModelScope.launch(ioDispatcher) { // <--- CAMBIO
            usuarioDao.borrarUsuario(nombreUsuario)
            if (viendoMedicos) cargarMedicos() else cargarPacientes()
        }
    }

    fun crearUsuario(nombre: String, usuario: String, pass: String, tlf: String) {
        if (nombre.isBlank() || usuario.isBlank() || pass.isBlank()) {
            errorDialogoAlta = "Rellene todos los campos obligatorios"
            return
        }

        val rolACrear = if (viendoMedicos && rolActual == "ADMIN") "MEDICO" else "PACIENTE"

        viewModelScope.launch(ioDispatcher) { // <--- CAMBIO
            val existe = usuarioDao.obtenerUsuario(usuario)
            if (existe != null) {
                // Para volver al hilo principal usamos Main explícitamente o dejamos que Compose lo maneje
                withContext(Dispatchers.Main) { errorDialogoAlta = "El ID de usuario ya existe" }
            } else {
                usuarioDao.registrarUsuario(
                    Usuario(
                        nombreCompleto = nombre,
                        usuario = usuario,
                        contrasena = pass,
                        telefono = tlf,
                        rol = rolACrear
                    )
                )
                if (viendoMedicos) cargarMedicos() else cargarPacientes()
                withContext(Dispatchers.Main) {
                    mostrarDialogoAlta = false
                    errorDialogoAlta = null
                }
            }
        }
    }
}