package com.example.vibracion_morse.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibracion_morse.datos.AppDatabase
import com.example.vibracion_morse.datos.Chat
import com.example.vibracion_morse.datos.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val chatDao = db.chatDao()
    private val usuarioDao = db.usuarioDao()

    private val _misChats = MutableStateFlow<List<Chat>>(emptyList())
    val misChats = _misChats.asStateFlow()

    private val _listaPacientes = MutableStateFlow<List<Usuario>>(emptyList())
    val listaPacientes = _listaPacientes.asStateFlow()

    var esUsuarioAdmin by mutableStateOf(false)

    var mostrarDialogoChat by mutableStateOf(false)
    var errorDialogoChat by mutableStateOf<String?>(null)

    var mostrarDialogoAlta by mutableStateOf(false)
    var errorDialogoAlta by mutableStateOf<String?>(null)

    fun inicializar(miUsuario: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = usuarioDao.obtenerUsuario(miUsuario)
            esUsuarioAdmin = user?.esAdmin == true

            if (esUsuarioAdmin) {
                cargarPacientes()
            } else {
                cargarChats(miUsuario)
            }
        }
    }

    private suspend fun cargarPacientes() {
        val pacientes = usuarioDao.obtenerTodosLosPacientes()
        _listaPacientes.value = pacientes
    }

    private suspend fun cargarChats(miUsuario: String) {
        chatDao.obtenerMisChats(miUsuario).collect { lista ->
            _misChats.value = lista
        }
    }

    fun borrarPaciente(nombreUsuario: String) {
        viewModelScope.launch(Dispatchers.IO) {
            usuarioDao.borrarUsuario(nombreUsuario)
            cargarPacientes()
        }
    }

    fun crearPaciente(nombre: String, usuario: String, pass: String, tlf: String) {
        if (nombre.isBlank() || usuario.isBlank() || pass.isBlank()) {
            errorDialogoAlta = "Rellene todos los campos obligatorios"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val existe = usuarioDao.obtenerUsuario(usuario)
            if (existe != null) {
                withContext(Dispatchers.Main) { errorDialogoAlta = "El ID de usuario ya existe" }
            } else {
                usuarioDao.registrarUsuario(
                    Usuario(
                        nombreCompleto = nombre,
                        usuario = usuario,
                        contrasena = pass,
                        telefono = tlf,
                        esAdmin = false
                    )
                )
                cargarPacientes()
                withContext(Dispatchers.Main) {
                    mostrarDialogoAlta = false
                    errorDialogoAlta = null
                }
            }
        }
    }

    fun intentarCrearChat(miUsuario: String, nombreContacto: String) {
        if (miUsuario == nombreContacto) {
            errorDialogoChat = "No puedes crear un chat contigo mismo"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val usuarioDestino = usuarioDao.obtenerUsuario(nombreContacto)

            if (usuarioDestino == null) {
                withContext(Dispatchers.Main) { errorDialogoChat = "El usuario no existe" }
            } else {
                val chatParaMi = chatDao.existeChat(miUsuario, nombreContacto)
                if (chatParaMi == null) {
                    chatDao.insertarChat(Chat(usuarioPropietario = miUsuario, usuarioContacto = nombreContacto))
                }

                val chatParaEl = chatDao.existeChat(nombreContacto, miUsuario)
                if (chatParaEl == null) {
                    chatDao.insertarChat(Chat(usuarioPropietario = nombreContacto, usuarioContacto = miUsuario))
                }

                withContext(Dispatchers.Main) {
                    mostrarDialogoChat = false
                    errorDialogoChat = null
                }
            }
        }
    }
}