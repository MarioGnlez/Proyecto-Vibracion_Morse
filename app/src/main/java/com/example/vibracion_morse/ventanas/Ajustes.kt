package com.example.vibracion_morse.ventanas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaAjustes(
    irHome: () -> Unit
) {
    // Variables locales
    var sliderLetrasPos by remember { mutableFloatStateOf(0.3f) }
    var sliderPalabrasPos by remember { mutableFloatStateOf(0.5f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Configuración",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Sección: Letras
            Text(
                text = "Duración entre letras",
                style = MaterialTheme.typography.titleMedium
            )

            Slider(
                value = sliderLetrasPos,
                onValueChange = { sliderLetrasPos = it },
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sección: Palabras
            Text(
                text = "Duración entre palabras",
                style = MaterialTheme.typography.titleMedium
            )

            Slider(
                value = sliderPalabrasPos,
                onValueChange = { sliderPalabrasPos = it },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { irHome() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Misma altura que el botón de Home
        ) {
            Text("VOLVER")
        }
    }
}