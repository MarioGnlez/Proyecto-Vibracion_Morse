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

    // Estado de la pantalla: ¿Estamos registrando o logueando?
    var esModoRegistro by mutableStateOf(false)

    var error by mutableStateOf<String?>(null)

    fun onAccionClick(onSuccess: (String) -> Unit) {
        if (usuario.isBlank() || contrasena.isBlank()) {
            error = "Usuario y contraseña son obligatorios"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (esModoRegistro) {
                // --- LOGICA DE REGISTRO ---
                if (nombreCompleto.isBlank() || telefono.isBlank()) {
                    withContext(Dispatchers.Main) { error = "Rellena todos los campos" }
                    return@launch
                }

                // 1. Comprobar si ya existe el usuario
                val existe = dao.obtenerUsuario(usuario)
                if (existe != null) {
                    withContext(Dispatchers.Main) { error = "El usuario ya existe, elige otro" }
                } else {
                    // 2. Crear usuario
                    dao.registrarUsuario(Usuario(
                        nombreCompleto = nombreCompleto,
                        telefono = telefono,
                        usuario = usuario,
                        contrasena = contrasena
                    ))
                    withContext(Dispatchers.Main) { onSuccess(usuario) }
                }

            } else {
                // --- LOGICA DE LOGIN ---
                val usuarioLogueado = dao.login(usuario, contrasena)
                if (usuarioLogueado != null) {
                    withContext(Dispatchers.Main) { onSuccess(usuario) }
                } else {
                    withContext(Dispatchers.Main) { error = "Usuario o contraseña incorrectos" }
                }
            }
        }
    }

    // Cambiar de modo limpia los errores
    fun cambiarModo() {
        esModoRegistro = !esModoRegistro
        error = null
    }
}