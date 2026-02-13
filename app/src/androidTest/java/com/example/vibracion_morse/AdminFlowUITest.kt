package com.example.vibracion_morse

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdminFlowUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun admin_puedeCrearNuevoMedico() {
        // 1. LOGIN COMO ADMIN
        composeTestRule.onNodeWithText("Usuario").performTextInput("admin")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("admin")
        composeTestRule.onNodeWithText("INICIAR SESIÓN").performClick()

        // 2. VERIFICAR QUE ESTAMOS EN VISTA DE MÉDICOS
        // El botón "MÉDICOS" debería existir y estar seleccionado (visualmente difícil de probar selección,
        // pero probamos que podemos interactuar).
        composeTestRule.onNodeWithText("Gestionando Médicos").assertIsDisplayed()

        // 3. ABRIR DIÁLOGO DE ALTA (Botón +)
        composeTestRule.onNodeWithContentDescription("Nuevo Usuario").performClick()

        // 4. RELLENAR DATOS DEL NUEVO MÉDICO
        // Usamos un nombre único para evitar conflictos si corres el test varias veces
        val nuevoId = "medicoTest${System.currentTimeMillis()}"

        composeTestRule.onNodeWithText("Nombre Completo").performTextInput("Doctor Test")
        composeTestRule.onNodeWithText("ID Usuario").performTextInput(nuevoId)
        composeTestRule.onNodeWithText("Contraseña").performTextInput("1234")
        composeTestRule.onNodeWithText("Teléfono").performTextInput("600000000")

        // 5. CONFIRMAR REGISTRO
        composeTestRule.onNodeWithText("REGISTRAR").performClick()

        // 6. VERIFICAR QUE APARECE EN LA LISTA
        // Buscamos en la lista el ID que acabamos de crear
        // useUnmergedTree = true ayuda a encontrar texto dentro de listas complejas
        composeTestRule.onNodeWithText("ID: $nuevoId | Rol: MEDICO", useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun admin_puedeCambiarVistaAPacientes() {
        // 1. LOGIN
        composeTestRule.onNodeWithText("Usuario").performTextInput("admin")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("admin")
        composeTestRule.onNodeWithText("INICIAR SESIÓN").performClick()

        // 2. CAMBIAR A PESTAÑA PACIENTES
        composeTestRule.onNodeWithText("PACIENTES").performClick()

        // 3. VERIFICAR CAMBIO DE TÍTULO O CONTENIDO
        composeTestRule.onNodeWithText("Gestionando Pacientes").assertIsDisplayed()

        // Debería aparecer "Juan Paciente" (paciente1) que viene por defecto
        composeTestRule.onNodeWithText("Juan Paciente").assertIsDisplayed()
    }
}