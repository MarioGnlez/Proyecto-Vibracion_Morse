package com.example.vibracion_morse.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

    private val _misChats = MutableStateFlow<List<Chat>>(emptyList())
    val misChats = _misChats.asStateFlow()

    var mostrarDialogo by mutableStateOf(false)
    var errorDialogo by mutableStateOf<String?>(null)

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
            val usuarioDestino = usuarioDao.obtenerUsuario(nombreContacto)

            if (usuarioDestino == null) {
                withContext(Dispatchers.Main) { errorDialogo = "El usuario no existe" }
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
                    mostrarDialogo = false
                    errorDialogo = null
                }
            }
        }
    }
}