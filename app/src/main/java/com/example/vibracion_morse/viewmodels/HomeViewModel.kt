package com.example.vibracion_morse.ventanas

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibracion_morse.dao.ChatDao
import com.example.vibracion_morse.dao.UsuarioDao
import com.example.vibracion_morse.datos.AppDatabase
import com.example.vibracion_morse.datos.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val chatDao = db.chatDao()
    private val usuarioDao = db.usuarioDao()

    // Estado de la lista de chats
    private val _misChats = MutableStateFlow<List<Chat>>(emptyList())
    val misChats = _misChats.asStateFlow()

    // Estado para el popup de añadir
    var mostrarDialogo by mutableStateOf(false)
    var errorDialogo by mutableStateOf<String?>(null)

    // Cargamos los chats cuando sepamos quién es el usuario
    fun cargarChats(miUsuario: String) {
        viewModelScope.launch {
            chatDao.obtenerMisChats(miUsuario).collect { lista ->
                _misChats.value = lista
            }
        }
    }

    fun intentarCrearChat(miUsuario: String, nombreContacto: String) {
        if (miUsuario == nombreContacto) {
            errorDialogo = "No puedes crear un chat contigo mismo"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            // 1. Verificar si el usuario destino existe
            val usuarioDestino = usuarioDao.obtenerUsuario(nombreContacto)

            if (usuarioDestino == null) {
                withContext(Dispatchers.Main) { errorDialogo = "El usuario no existe" }
            } else {
                // 2. Verificar si ya tenemos chat con él
                val chatExistente = chatDao.existeChat(miUsuario, nombreContacto)
                if (chatExistente != null) {
                    withContext(Dispatchers.Main) { errorDialogo = "Ya tienes un chat con este usuario" }
                } else {
                    // 3. Crear el chat
                    chatDao.insertarChat(Chat(usuarioPropietario = miUsuario, usuarioContacto = nombreContacto))
                    withContext(Dispatchers.Main) {
                        mostrarDialogo = false // Cerrar dialogo
                        errorDialogo = null
                    }
                }
            }
        }
    }
}