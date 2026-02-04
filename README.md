<div align="center">
  <img src="assets/logo_banner.png" alt="Morse Chat Banner" width="100%">

  # 📳 Morse Chat
  
  ### Siente lo que escribes. Mensajería Táctil con Vibración.

  <img src="https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Architecture-MVVM-brightgreen?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Database-Room-orange?style=for-the-badge&logo=sqlite&logoColor=white" />
</div>

---

## 💡 Sobre el Proyecto

**Morse Chat** es una aplicación nativa de Android que reinterpreta la mensajería instantánea añadiendo una capa sensorial: **la vibración háptica**. 

La aplicación no solo permite chatear, sino que **traduce los mensajes de texto a patrones de vibración (Código Morse)** en tiempo real. Esto permite a los usuarios "leer" mensajes mediante el tacto, una funcionalidad pensada para la accesibilidad y la comunicación discreta.

Actualmente funciona como una **Demo Técnica Avanzada**, simulando un entorno de servidor mediante una base de datos local robusta (**Room**), gestionando usuarios, sesiones y persistencia de chat.

---

## 📸 Galería de Vistas

> *La interfaz sigue las guías de Material Design 3, priorizando la claridad y la accesibilidad.*

| **Acceso Seguro** | **Registro de Usuarios** |
|:---:|:---:|
| <img src="assets/captura_login.png" width="250" alt="Pantalla de Login" /> | <img src="assets/captura_registro.png" width="250" alt="Pantalla de Registro" /> |
| *Inicio de sesión validado.* | *Formulario de creación de cuenta.* |

| **Mis Conversaciones** | **Chat Interactivo** |
|:---:|:---:|
| <img src="assets/captura_home.png" width="250" alt="Pantalla Home" /> | <img src="assets/captura_chats.png" width="250" alt="Pantalla de Chat" /> |
| *Lista de contactos. Un toque vibra el nombre.* | *Envío y recepción. Toca el mensaje para sentirlo.* |

| **Traductor Manual** | **Ajustes de Vibración** |
|:---:|:---:|
| <img src="assets/captura_traductor.png" width="250" alt="Traductor Manual" /> | <img src="assets/captura_ajustes.png" width="250" alt="Ajustes" /> |
| *Playground: Escribe y transmite vibración.* | *Calibración precisa de la velocidad del Morse.* |

---

## 🚀 Funcionalidades Clave

### 📳 Motor Háptico Morse
- **Traducción en tiempo real:** Algoritmo optimizado para convertir `String` -> `Patrón de Vibración`.
- **Compatibilidad Dual:** Soporte para APIs antiguas (`Vibrator`) y modernas (`VibratorManager` en Android 12+).
- **Control de Velocidad:** El usuario puede definir en milisegundos la duración entre "Puntos", "Rayas" y "Espacios" desde los ajustes.

### 💾 Arquitectura de Datos (Local)
- **Persistencia con Room:** Base de datos SQLite abstracta.
- **Relaciones:** Sistema relacional completo entre Usuarios y Mensajes.
- **Lógica Bidireccional:** Simulación de backend; al crear un chat desde el "Usuario A", se genera automáticamente la entrada inversa para el "Usuario B".

### 👆 Experiencia de Usuario (UX)
- **Toque para leer:** Al pulsar cualquier mensaje en el chat, el teléfono vibra el patrón: *"{Nombre} DIJO {Mensaje}"*.
- **Identificación háptica:** En la lista de contactos, un toque corto vibra el nombre del usuario para identificarlo sin mirar.

---

## 🛠️ Stack Tecnológico

El proyecto está construido siguiendo las mejores prácticas de desarrollo moderno en Android:

* **Lenguaje:** [Kotlin](https://kotlinlang.org/) (100%)
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Declarativa)
* **Arquitectura:** MVVM (Model-View-ViewModel)
* **Inyección de Dependencias:** ViewModel Factory manual (preparado para Hilt/Koin).
* **Asincronía:** Coroutines & Kotlin Flows.
* **Navegación:** Jetpack Navigation Compose con paso de argumentos tipados.

### 🗄️ Esquema de Base de Datos

```mermaid
erDiagram
    USUARIO ||--o{ CHAT : tiene
    USUARIO ||--o{ MENSAJE : envia
    USUARIO {
        int id PK
        string usuario UK
        string password
        string telefono
    }
    CHAT {
        int id PK
        string propietario
        string contacto
    }
    MENSAJE {
        int id PK
        string remitente
        string destinatario
        string texto
        long timestamp
    }
