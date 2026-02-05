package com.example.vibracion_morse.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibracion_morse.datos.AppDatabase
import com.example.vibracion_morse.datos.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).usuarioDao()

    // Variables para guardar lo que escribes en las cajas de texto
    var usuario by mutableStateOf("")
    var contrasena by mutableStateOf("")

    // Estos solo se usan si te estás registrando
    var nombreCompleto by mutableStateOf("")
    var telefono by mutableStateOf("")

    // Controla si estamos viendo la pantalla de Login o la de Registro
    var esModoRegistro by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    // Esto se ejecuta nada más abrir la pantalla
    init {
        asegurarUsuariosDePrueba()
    }

    // Crea usuarios falsos de prueba si es la primera vez que abres la app
    private fun asegurarUsuariosDePrueba() {
        viewModelScope.launch(Dispatchers.IO) {
            if (dao.obtenerUsuario("usuario1") == null) {
                dao.registrarUsuario(Usuario(
                    nombreCompleto = "Usuario Uno Prueba",
                    telefono = "111111111",
                    usuario = "usuario1",
                    contrasena = "1234"
                ))
            }
            if (dao.obtenerUsuario("usuario2") == null) {
                dao.registrarUsuario(Usuario(
                    nombreCompleto = "Usuario Dos Prueba",
                    telefono = "222222222",
                    usuario = "usuario2",
                    contrasena = "1234"
                ))
            }
        }
    }

    // Qué pasa cuando pulsas el botón "Entrar" o "Registrarse"
    fun onAccionClick(onSuccess: (String) -> Unit) {
        // Quitamos espacios en blanco por si acaso
        val usuarioLimpio = usuario.trim()
        val passLimpio = contrasena.trim()

        if (usuarioLimpio.isBlank() || passLimpio.isBlank()) {
            error = "Usuario y contraseña son obligatorios"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (esModoRegistro) {
                // --- MODO REGISTRO ---
                if (nombreCompleto.isBlank() || telefono.isBlank()) {
                    withContext(Dispatchers.Main) { error = "Rellena todos los campos" }
                    return@launch
                }

                // Comprobamos si ya existe alguien con ese nombre
                val existe = dao.obtenerUsuario(usuarioLimpio)
                if (existe != null) {
                    withContext(Dispatchers.Main) { error = "El usuario ya existe, elige otro" }
                } else {
                    // Si no existe, lo guardamos
                    dao.registrarUsuario(Usuario(
                        nombreCompleto = nombreCompleto,
                        telefono = telefono,
                        usuario = usuarioLimpio,
                        contrasena = passLimpio
                    ))
                    withContext(Dispatchers.Main) { onSuccess(usuarioLimpio) }
                }

            } else {
                // --- MODO LOGIN ---
                val usuarioLogueado = dao.login(usuarioLimpio, passLimpio)

                if (usuarioLogueado != null) {
                    // Login correcto
                    withContext(Dispatchers.Main) { onSuccess(usuarioLimpio) }
                } else {
                    // Contraseña o usuario mal
                    withContext(Dispatchers.Main) { error = "Usuario o contraseña incorrectos" }
                }
            }
        }
    }

    // Cambia entre la vista de Login y la de Registro
    fun cambiarModo() {
        esModoRegistro = !esModoRegistro
        error = null
    }
}