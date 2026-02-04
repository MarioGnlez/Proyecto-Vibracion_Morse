package com.example.vibracion_morse.ventanas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vibracion_morse.viewmodels.LoginViewModel

@Composable
fun PantallaLogin(
    onLoginSuccess: (String) -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val CelestePrincipal = Color(0xFF4DD0E1)
    val BlancoFondo = Color(0xFFFFFFFF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlancoFondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título dinámico
        Text(
            text = if (viewModel.esModoRegistro) "Crear Cuenta" else "Iniciar Sesión",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = CelestePrincipal
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // CAMPOS COMUNES (Usuario y Contraseña)
                OutlinedTextField(
                    value = viewModel.usuario,
                    onValueChange = { viewModel.usuario = it; viewModel.error = null },
                    label = { Text("Usuario") },
                    leadingIcon = { Icon(Icons.Default.Face, null, tint = CelestePrincipal) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = viewModel.contrasena,
                    onValueChange = { viewModel.contrasena = it; viewModel.error = null },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = CelestePrincipal) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation(), // Ocultar contraseña
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                // CAMPOS SOLO REGISTRO
                if (viewModel.esModoRegistro) {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = viewModel.nombreCompleto,
                        onValueChange = { viewModel.nombreCompleto = it },
                        label = { Text("Nombre Completo") },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = CelestePrincipal) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = viewModel.telefono,
                        onValueChange = { viewModel.telefono = it },
                        label = { Text("Teléfono") },
                        leadingIcon = { Icon(Icons.Default.Phone, null, tint = CelestePrincipal) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.error != null) {
            Text(
                text = viewModel.error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // BOTÓN PRINCIPAL
        Button(
            onClick = { viewModel.onAccionClick(onLoginSuccess) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (viewModel.esModoRegistro) "REGISTRARSE" else "ENTRAR",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BOTÓN CAMBIO DE MODO
        Text(
            text = if (viewModel.esModoRegistro) "¿Ya tienes cuenta? Inicia sesión aquí" else "¿No tienes cuenta? Regístrate aquí",
            color = Color.Gray,
            modifier = Modifier
                .clickable { viewModel.cambiarModo() }
                .padding(8.dp)
        )
    }
}