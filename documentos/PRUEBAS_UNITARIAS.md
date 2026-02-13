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
* ✅ **`login permite acceso a ADMIN`**: Verifica que un usuario con rol "ADMIN" recibe el callback de éxito.
* ✅ **`login BLOQUEA acceso a PACIENTE`**: Verifica que, aunque la contraseña sea correcta, si el rol es "PACIENTE", el sistema devuelve un error y no permite el acceso.

### 1.2 HomeViewModelTest
Valida la lógica de carga de datos según el rol.
* ✅ **`inicializar como ADMIN carga medicos`**: Asegura que al entrar un administrador, la variable `viendoMedicos` se activa por defecto.
* ✅ **`inicializar como MEDICO carga pacientes`**: Asegura que el médico ve directamente la lista de pacientes.
* ✅ **`crearUsuario`**: Valida que el método llama correctamente al DAO para insertar el nuevo registro.

> **Nota Técnica:** Se utiliza `UnconfinedTestDispatcher` inyectado en los ViewModels para evitar condiciones de carrera (Race Conditions) y errores de hilos (`MainLooper`) durante los tests.

---

## 2. Pruebas Instrumentadas (UI / End-to-End)
Se encuentran en el paquete `(androidTest)`. Validan los flujos de usuario completos ejecutándose en un entorno Android real/emulado.

### 2.1 LoginUITest
Simula un usuario escribiendo en la pantalla de login.
* **Escenario 1:** Escribe credenciales de Admin -> Verifica que aparece el texto "Panel Administrador".
* **Escenario 2:** Escribe credenciales de Paciente -> Verifica que aparece el mensaje de error de seguridad en pantalla.

### 2.2 AdminFlowUITest
Prueba las capacidades de gestión del administrador.
* **Flujo:** Login Admin -> Pulsar botón "+" -> Rellena formulario de Médico -> Pulsar Registrar -> Verifica que el nuevo médico aparece en la `LazyColumn`.
* **Navegación:** Verifica que al pulsar el botón "PACIENTES", la lista cambia correctamente.

### 2.3 MedicoFlowUITest
Prueba el flujo clínico crítico.
* **Flujo:** Login Médico -> Seleccionar Paciente ("HISTORIAL") -> Escribir nota de evolución -> Pulsar "GUARDAR" -> Verifica que la nota aparece persistente en la lista inferior y que el campo de texto se limpia.

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