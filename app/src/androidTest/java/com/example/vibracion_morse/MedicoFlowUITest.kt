package com.example.vibracion_morse

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MedicoFlowUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun medico_puedeAnadirHistorial() {
        // 1. LOGIN COMO MÉDICO (medico1 / 1234 viene en los datos semilla)
        composeTestRule.onNodeWithText("Usuario").performTextInput("medico1")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("1234")
        composeTestRule.onNodeWithText("INICIAR SESIÓN").performClick()

        // 2. VERIFICAR QUE VEO AL PACIENTE Y ENTRAR EN SU HISTORIAL
        // Buscamos el botón "HISTORIAL" asociado al paciente.
        // Como puede haber varios, cogemos el primero o filtramos por el padre.
        // Para simplificar, asumimos que "paciente1" está ahí.
        composeTestRule.onNodeWithText("HISTORIAL").performClick()

        // 3. ESTOY EN PANTALLA SEGUIMIENTO
        composeTestRule.onNodeWithText("Historial: paciente1").assertIsDisplayed()

        // 4. RELLENAR FORMULARIO DE SEGUIMIENTO
        // Nota: La fecha suele estar deshabilitada/auto, rellenamos profesional y nota.
        composeTestRule.onNodeWithText("Profesional / Empleado").performTextInput("Dr. Test")

        val textoNota = "Paciente evoluciona favorablemente test UI."
        composeTestRule.onNodeWithText("Escriba evolución del paciente...").performTextInput(textoNota)

        // 5. GUARDAR
        composeTestRule.onNodeWithText("GUARDAR REGISTRO").performClick()

        // 6. VERIFICAR QUE LA NOTA APARECE EN LA LISTA INFERIOR
        // ScrollTo asegura que si la lista es larga, baje hasta encontrarlo
        composeTestRule.onNodeWithText(textoNota).performScrollTo().assertIsDisplayed()

        // Verificamos que el campo de texto se ha limpiado después de guardar
        composeTestRule.onNodeWithText("Escriba evolución del paciente...")
            .assertTextEquals("") // El contenido debe estar vacío
    }
}