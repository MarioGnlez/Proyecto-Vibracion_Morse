package com.example.vibracion_morse.ventanas

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

    // Estados de los campos
    var usuario by mutableStateOf("")
    var contrasena by mutableStateOf("")

    // Campos solo para registro
    var nombreCompleto by mutableStateOf("")
    var telefono by mutableStateOf("")

    // Estado de la pantalla
    var esModoRegistro by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    // BLOQUE INIT: Se ejecuta nada más abrir la pantalla
    // Esto asegura que los usuarios de prueba existan antes de que te de tiempo a escribir
    init {
        asegurarUsuariosDePrueba()
    }

    private fun asegurarUsuariosDePrueba() {
        viewModelScope.launch(Dispatchers.IO) {
            // Si no existe usuario1, lo creamos ahora mismo
            if (dao.obtenerUsuario("usuario1") == null) {
                dao.registrarUsuario(Usuario(
                    nombreCompleto = "Usuario Uno Prueba",
                    telefono = "111111111",
                    usuario = "usuario1",
                    contrasena = "1234"
                ))
            }
            // Si no existe usuario2, lo creamos
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

    fun onAccionClick(onSuccess: (String) -> Unit) {
        // 1. Limpiamos espacios en blanco accidentales (SOLUCIÓN IMPORTANTE)
        val usuarioLimpio = usuario.trim()
        val passLimpio = contrasena.trim()

        if (usuarioLimpio.isBlank() || passLimpio.isBlank()) {
            error = "Usuario y contraseña son obligatorios"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (esModoRegistro) {
                // --- REGISTRO ---
                if (nombreCompleto.isBlank() || telefono.isBlank()) {
                    withContext(Dispatchers.Main) { error = "Rellena todos los campos" }
                    return@launch
                }

                val existe = dao.obtenerUsuario(usuarioLimpio)
                if (existe != null) {
                    withContext(Dispatchers.Main) { error = "El usuario ya existe, elige otro" }
                } else {
                    dao.registrarUsuario(Usuario(
                        nombreCompleto = nombreCompleto,
                        telefono = telefono,
                        usuario = usuarioLimpio,
                        contrasena = passLimpio
                    ))
                    withContext(Dispatchers.Main) { onSuccess(usuarioLimpio) }
                }

            } else {
                // --- LOGIN ---
                // Usamos los datos limpios (sin espacios)
                val usuarioLogueado = dao.login(usuarioLimpio, passLimpio)

                if (usuarioLogueado != null) {
                    withContext(Dispatchers.Main) { onSuccess(usuarioLimpio) }
                } else {
                    withContext(Dispatchers.Main) { error = "Usuario o contraseña incorrectos" }
                }
            }
        }
    }

    fun cambiarModo() {
        esModoRegistro = !esModoRegistro
        error = null
    }
}