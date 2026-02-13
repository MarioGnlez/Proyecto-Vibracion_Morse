package com.example.vibracion_morse

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginUITest {

    // Esta regla abre la aplicación (MainActivity) antes de cada test
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginComoAdmin_MuestraPanelAdmin() {
        // 1. Escribir credenciales de Admin
        // Asegúrate de que los textos "Usuario" y "Contraseña" coinciden con los label de tu Login
        composeTestRule.onNodeWithText("Usuario").performTextInput("admin")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("admin")

        // 2. Cerrar teclado (opcional, pero a veces ayuda a encontrar el botón)
        // composeTestRule.onRoot().performClick()

        // 3. Pulsar botón de entrar
        // Busca el botón por el texto que tenga dentro (ej: "INICIAR SESIÓN" o "ENTRAR")
        composeTestRule.onNodeWithText("INICIAR SESIÓN").performClick()

        // 4. VERIFICACIÓN: ¿Estamos en la pantalla de Admin?
        // Buscamos el texto del título superior
        composeTestRule.onNodeWithText("Panel Administrador").assertIsDisplayed()
    }

    @Test
    fun loginComoMedico_MuestraPanelMedico() {
        // 1. Escribir credenciales de Médico (datos semilla)
        composeTestRule.onNodeWithText("Usuario").performTextInput("medico1")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("1234")

        // 2. Pulsar botón
        composeTestRule.onNodeWithText("INICIAR SESIÓN").performClick()

        // 3. VERIFICACIÓN: ¿Estamos en la pantalla de Médico?
        composeTestRule.onNodeWithText("Panel Médico").assertIsDisplayed()
    }

    @Test
    fun loginComoPaciente_MuestraError() {
        // 1. Escribir credenciales de Paciente (datos semilla)
        composeTestRule.onNodeWithText("Usuario").performTextInput("paciente1")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("1234")

        // 2. Pulsar botón
        composeTestRule.onNodeWithText("INICIAR SESIÓN").performClick()

        // 3. VERIFICACIÓN: El mensaje de error debe aparecer
        // El texto debe coincidir con el que pusimos en LoginViewModel
        composeTestRule.onNodeWithText("Acceso denegado: Los pacientes no tienen acceso a la plataforma.")
            .assertIsDisplayed()
    }
}