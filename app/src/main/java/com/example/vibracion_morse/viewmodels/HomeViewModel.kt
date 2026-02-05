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

    // Conectamos con la base de datos para pedir datos de chats y usuarios
    private val db = AppDatabase.getDatabase(application)
    private val chatDao = db.chatDao()
    private val usuarioDao = db.usuarioDao()

    // Aquí guardamos la lista de chats para que la pantalla la vea
    private val _misChats = MutableStateFlow<List<Chat>>(emptyList())
    val misChats = _misChats.asStateFlow()

    // Variables para controlar si mostramos la ventana de "Nuevo Chat" y si hay errores
    var mostrarDialogo by mutableStateOf(false)
    var errorDialogo by mutableStateOf<String?>(null)

    // Esta función carga tus conversaciones desde la base de datos
    fun cargarChats(miUsuario: String) {
        viewModelScope.launch {
            chatDao.obtenerMisChats(miUsuario).collect { lista ->
                _misChats.value = lista
            }
        }
    }

    // Lógica para cuando intentas añadir un amigo nuevo
    fun intentarCrearChat(miUsuario: String, nombreContacto: String) {
        // No te dejamos hablar contigo mismo
        if (miUsuario == nombreContacto) {
            errorDialogo = "No puedes crear un chat contigo mismo"
            return
        }

        // Hacemos las comprobaciones en segundo plano
        viewModelScope.launch(Dispatchers.IO) {
            val usuarioDestino = usuarioDao.obtenerUsuario(nombreContacto)

            // Si el usuario no existe, avisamos
            if (usuarioDestino == null) {
                withContext(Dispatchers.Main) { errorDialogo = "El usuario no existe" }
            } else {
                // Si existe, comprobamos si ya teníais un chat abierto
                val chatParaMi = chatDao.existeChat(miUsuario, nombreContacto)
                if (chatParaMi == null) {
                    // Creamos el chat para ti
                    chatDao.insertarChat(Chat(usuarioPropietario = miUsuario, usuarioContacto = nombreContacto))
                }

                val chatParaEl = chatDao.existeChat(nombreContacto, miUsuario)
                if (chatParaEl == null) {
                    // Creamos el chat para la otra persona también (simulando que le aparece a él)
                    chatDao.insertarChat(Chat(usuarioPropietario = nombreContacto, usuarioContacto = miUsuario))
                }

                // Cerramos la ventanica
                withContext(Dispatchers.Main) {
                    mostrarDialogo = false
                    errorDialogo = null
                }
            }
        }
    }
}