package com.example.vibracion_morse.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibracion_morse.datos.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher // <--- IMPORTANTE
import kotlinx.coroutines.Dispatchers        // <--- IMPORTANTE
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).usuarioDao()

    // Variable inyectable para tests
    var ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    var usuario by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var error by mutableStateOf<String?>(null)

    fun login(onSuccess: (String) -> Unit) {
        val usuarioLimpio = usuario.trim()
        val passLimpio = contrasena.trim()

        if (usuarioLimpio.isBlank() || passLimpio.isBlank()) {
            error = "Usuario y contraseña son obligatorios"
            return
        }

        // Usamos la variable ioDispatcher
        viewModelScope.launch(ioDispatcher) {
            val usuarioLogueado = dao.login(usuarioLimpio, passLimpio)

            withContext(Dispatchers.Main) {
                if (usuarioLogueado != null) {
                    if (usuarioLogueado.rol == "PACIENTE") {
                        error = "Acceso denegado: Los pacientes no tienen acceso a la plataforma."
                    } else {
                        onSuccess(usuarioLimpio)
                    }
                } else {
                    error = "Credenciales incorrectas"
                }
            }
        }
    }
}