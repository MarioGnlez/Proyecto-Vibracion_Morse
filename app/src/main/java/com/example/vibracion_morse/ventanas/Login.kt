package com.example.vibracion_morse.ventanas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
        Text(
            text = "Acceso Clínica",
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
                OutlinedTextField(
                    value = viewModel.usuario,
                    onValueChange = { viewModel.usuario = it; viewModel.error = null },
                    label = { Text("Usuario / ID") },
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
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )
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

        Button(
            onClick = { viewModel.login(onLoginSuccess) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CelestePrincipal),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "ENTRAR",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}