package com.example.vibracion_morse.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibracion_morse.datos.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).usuarioDao()

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

        viewModelScope.launch(Dispatchers.IO) {
            val usuarioLogueado = dao.login(usuarioLimpio, passLimpio)

            if (usuarioLogueado != null) {
                withContext(Dispatchers.Main) { onSuccess(usuarioLimpio) }
            } else {
                withContext(Dispatchers.Main) { error = "Credenciales incorrectas" }
            }
        }
    }
}