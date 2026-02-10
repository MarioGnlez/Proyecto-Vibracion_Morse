<div align="center">
  <img src="fotos-documentacion/logo_banner.png" alt="Morse Chat Banner" width="100%">

  <img src="https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Architecture-MVVM-brightgreen?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Database-Room-orange?style=for-the-badge&logo=sqlite&logoColor=white" />
</div>

---

## 🏥 Descripción del Proyecto: Herramienta de Gestión Clínica y Comunicación Asistiva

**Morse Chat** ha evolucionado de una simple aplicación de mensajería a una **herramienta integral para entornos clínicos**. Su objetivo es facilitar la comunicación y el seguimiento de pacientes con diversidad funcional (visual o auditiva) mediante **vibración háptica**.

El sistema diferencia dos roles claros:
1.  **Administrador (Profesional):** Gestiona altas/bajas de pacientes y realiza el **seguimiento clínico** (historial de evolución).
2.  **Paciente:** Utiliza la app como herramienta de comunicación asistiva (Traductor Morse y Chat).

---

## 📹 Demo en Vídeo
[▶️ Ver Vídeo Explicativo del Proyecto (Google Drive)](https://drive.google.com/file/d/1GlHzIxxDlw2xY5wd2-xpRFyVL3Sz3R_O/view?usp=sharing)

---

# 📝 Memoria Técnica por Criterios de Evaluación (RA)

A continuación, se justifica el cumplimiento de los Resultados de Aprendizaje mediante la implementación realizada.

## RA1. Interfaz Gráfica y Código

### RA1.a Analiza herramientas y librerías
Se ha utilizado **Android Studio Ladybug** como IDE oficial. El proyecto se basa en **Kotlin** y utiliza **Jetpack Compose** para la UI moderna, prescindiendo de XML. Para la persistencia de datos (Pacientes, Chats, Seguimientos) se ha implementado **Room Database** por su robustez y seguridad (Sandboxing).

### RA1.b Crea interfaz gráfica
La interfaz es **adaptativa según el rol**.
* **Vista Admin:** Panel de gestión con lista de pacientes y botones de acción rápida (Email, Info, Borrar).
* **Vista Paciente:** Interfaz simplificada con botones grandes para Chat y Traductor.

| **Login / Alta** | **Vista Admin (Gestión)** |
|:---:|:---:|
| <img src="fotos-documentacion/captura_login.png" width="250" alt="Login" /> | <img src="fotos-documentacion/captura_admin.png" width="250" alt="Panel Admin" /> |

### RA1.c Uso de layouts y posicionamiento
Se utiliza `Scaffold` para la estructura base (TopBar). Las listas (pacientes o mensajes) emplean `LazyColumn` para un rendimiento óptimo. Se usan `Row` y `Column` con `Arrangement.SpaceBetween` y `weights` para asegurar que los botones se distribuyen equitativamente en pantalla.

### RA1.d Personalización de componentes
Se ha definido una paleta de colores personalizada con **Cian (`#4DD0E1`)** como color primario para garantizar alto contraste y accesibilidad. Los botones y tarjetas (`Card`) tienen bordes redondeados (`RoundedCornerShape(16.dp)`) para una estética amigable.

### RA1.e Análisis del código
El proyecto sigue estrictamente la arquitectura **MVVM (Model-View-ViewModel)**:
* **Datos:** `Usuario`, `Seguimiento`, `AppDatabase`.
* **ViewModel:** `HomeViewModel` (lógica de roles), `SeguimientoViewModel` (lógica clínica).
* **Vista:** Pantallas en Compose (`Home`, `PantallaSeguimiento`).

### RA1.f Modificación del código
La modularidad ha permitido escalar la app fácilmente. Se añadió la entidad `Seguimiento` y su DAO sin romper la lógica del Chat existente, gracias a la inyección de dependencias manual en el `ViewModel`.

### RA1.g Asociación de eventos
La interacción es fluida mediante eventos `onClick`. Ejemplo: Al pulsar "Guardar Registro" en el seguimiento, se dispara una corrutina que guarda la fecha, profesional y nota, limpiando el formulario automáticamente al finalizar (`onSuccess`).

### RA1.h App integrada
Todos los módulos (Login -> Home -> Chat/Seguimiento) comparten la misma base de datos y sesión, permitiendo una experiencia unificada.

---

## RA2. Interfases Naturales de Usuario (NUI)

### RA2.a Herramientas NUI
Se utiliza la API **`VibratorManager`** (Android 12+) y **`Vibrator`** (Legacy) para controlar el hardware háptico del dispositivo.

### RA2.b Diseño conceptual NUI
El concepto central es la **Traducción Háptica**: convertir texto digital (`String`) en impulsos físicos (vibraciones). Esto permite "leer con la piel".

### RA2.c Interacción por voz
No se incluye deliberadamente para favorecer la privacidad en entornos clínicos o silenciosos, sustituyéndose por la vibración.

### RA2.d Interacción por gesto
Se implementan gestos táctiles simples: **Toque simple** sobre un mensaje o contacto dispara la lectura háptica.

---

## RA3. Componentes

### RA3.a Herramientas de componentes
Uso extensivo de **Material3** de Jetpack Compose (`OutlinedTextField`, `CardDefaults`, `FloatingActionButton`).

### RA3.b Componentes reutilizables
Las tarjetas de información (usadas tanto para mostrar Pacientes en el panel Admin como Mensajes en el Chat) comparten estructura de diseño.

### RA3.c Parámetros y defaults
Los componentes reciben parámetros tipados y lambdas. Ejemplo:
```kotlin
fun PantallaSeguimiento(
    pacienteId: String, // Parámetro obligatorio
    irAtras: () -> Unit // Lambda para navegación
)
