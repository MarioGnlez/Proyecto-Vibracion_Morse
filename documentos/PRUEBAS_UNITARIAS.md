# 🧪 Documentación de Pruebas y Calidad de Software

Este documento detalla la estrategia de validación técnica implementada en el proyecto **Morse Chat SGC**. Se han realizado dos niveles de pruebas: Unitarias (Lógica) e Instrumentadas (Interfaz de Usuario).

## 🛠️ Tecnologías de Testing Utilizadas
* **JUnit 4:** Framework base para la ejecución de pruebas.
* **Mockk:** Librería para simular dependencias (Mocks) como la Base de Datos y DAOs.
* **Kotlinx Coroutines Test:** Para controlar la ejecución de hilos asíncronos (`runTest`, `UnconfinedTestDispatcher`).
* **Jetpack Compose UI Test:** Para la automatización de interacciones en el emulador.

---

## 1. Pruebas Unitarias (Lógica de Negocio)
Se encuentran en el paquete `(test)`. Validan que los ViewModels procesen los datos correctamente sin necesidad de ejecutar la app en un dispositivo.

### 1.1 LoginViewModelTest
Valida la seguridad y el control de acceso.

* **Caso de Prueba: Bloqueo de Pacientes**
  Verifica que, aunque la contraseña sea correcta, si el rol es "PACIENTE", el sistema devuelve un error de seguridad y no permite el acceso.

```kotlin
@Test
fun `login BLOQUEA acceso a PACIENTE`() = runTest {
    // DADO un usuario con rol PACIENTE en la base de datos simulada
    val pacienteUser = Usuario(2, "Juan", "666", "paciente1", "1234", "PACIENTE")
    coEvery { usuarioDao.login("paciente1", "1234") } returns pacienteUser

    // CUANDO intenta loguearse
    var loginExitoso = false
    viewModel.login { loginExitoso = true } // Callback de éxito

    // ENTONCES el sistema debe denegar el acceso
    assertEquals(false, loginExitoso)
    assertEquals("Acceso denegado: Los pacientes no tienen acceso a la plataforma.", viewModel.error)
}
```

### 1.2 HomeViewModelTest
Valida la lógica de carga de datos según el rol del usuario logueado.

* **Caso de Prueba: Inicialización de Admin**
  Asegura que al entrar un administrador, la variable de estado se configura para gestionar médicos por defecto.

```kotlin
@Test
fun `inicializar como ADMIN carga medicos por defecto`() = runTest {
    // DADO un usuario Admin
    val adminUser = Usuario(1, "SuperAdmin", "000", "admin", "pass", "ADMIN")
    coEvery { usuarioDao.obtenerUsuario("admin") } returns adminUser
    
    // CUANDO se inicializa el ViewModel
    viewModel.inicializar("admin")
    
    // ENTONCES el rol se actualiza y la vista cambia a Médicos
    assertEquals("ADMIN", viewModel.rolActual)
    // Verificamos que se llamó al DAO buscando usuarios con rol MEDICO
    coVerify { usuarioDao.obtenerUsuariosPorRol("MEDICO") }
}
```

> **Nota Técnica:** Se utiliza `UnconfinedTestDispatcher` inyectado en los ViewModels para evitar condiciones de carrera (Race Conditions) y errores de hilos (`MainLooper`) durante los tests.

---

## 2. Pruebas Instrumentadas (UI / End-to-End)
Se encuentran en el paquete `(androidTest)`. Validan los flujos de usuario completos ejecutándose en un entorno Android real/emulado. Utilizan la regla `createAndroidComposeRule`.

### 2.1 LoginUITest
Simula un usuario escribiendo en la pantalla de login e interactuando con los botones.

```kotlin
@Test
fun loginComoAdmin_MuestraPanelAdmin() {
    // 1. Escribir credenciales en la UI
    composeTestRule.onNodeWithText("Usuario").performTextInput("admin")
    composeTestRule.onNodeWithText("Contraseña").performTextInput("admin")

    // 2. Pulsar botón
    composeTestRule.onNodeWithText("INICIAR SESIÓN").performClick()

    // 3. VERIFICACIÓN: ¿Estamos en la pantalla de Admin?
    composeTestRule.onNodeWithText("Panel Administrador").assertIsDisplayed()
}
```

### 2.2 AdminFlowUITest
Prueba la capacidad del administrador para crear nuevos profesionales en el sistema.

```kotlin
@Test
fun admin_puedeCrearNuevoMedico() {
    // ... Login previo ...

    // Abrir diálogo y rellenar datos
    composeTestRule.onNodeWithContentDescription("Nuevo Usuario").performClick()
    composeTestRule.onNodeWithText("Nombre Completo").performTextInput("Doctor Test")
    
    // Confirmar registro
    composeTestRule.onNodeWithText("REGISTRAR").performClick()

    // Verificar que aparece en la lista (LazyColumn)
    composeTestRule.onNodeWithText("ID: medicoTest | Rol: MEDICO", useUnmergedTree = true)
        .assertExists()
}
```

### 2.3 MedicoFlowUITest
Prueba el flujo clínico crítico: acceder al historial de un paciente y guardar una nota de evolución.

```kotlin
@Test
fun medico_puedeAnadirHistorial() {
    // ... Login como médico ...

    // Entrar en historial del paciente
    composeTestRule.onNodeWithText("HISTORIAL").performClick()

    // Escribir nota clínica
    val textoNota = "Paciente evoluciona favorablemente."
    composeTestRule.onNodeWithText("Escriba evolución del paciente...").performTextInput(textoNota)

    // Guardar
    composeTestRule.onNodeWithText("GUARDAR REGISTRO").performClick()

    // Verificar persistencia en la lista inferior
    composeTestRule.onNodeWithText(textoNota).performScrollTo().assertIsDisplayed()
}
```

---

## 3. Cómo ejecutar las pruebas

### Desde Android Studio
1.  **Unitarias:** Haga clic derecho sobre la carpeta `com.example.vibracion_morse (test)` y seleccione **Run Tests**.
2.  **Instrumentadas:** Con un emulador abierto, haga clic derecho sobre `com.example.vibracion_morse (androidTest)` y seleccione **Run Tests**.

### Resultados Esperados
Todas las pruebas deben finalizar con el indicador en **VERDE**.

| Tipo | Total Tests | Estado |
| :--- | :---: | :---: |
| Unitarias | 6 | ✅ PASSED |
| Instrumentadas | 5 | ✅ PASSED |
