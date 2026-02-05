<div align="center">
  <img src="fotos-documentacion/logo_banner.png" alt="Morse Chat Banner" width="100%">

  <img src="https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Architecture-MVVM-brightgreen?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Database-Room-orange?style=for-the-badge&logo=sqlite&logoColor=white" />
</div>

---

## Descripción General del Proyecto

**Morse Chat** es una aplicación nativa de Android diseñada para la mensajería instantánea mediante **vibración háptica**. La aplicación traduce los mensajes de texto a patrones de vibración (Código Morse) en tiempo real, permitiendo a los usuarios interpretar la información mediante el tacto.

Esta funcionalidad responde a una necesidad de accesibilidad y comunicación discreta, eliminando la dependencia visual o auditiva. Actualmente funciona con una arquitectura de base de datos local (**Room**) que simula la persistencia y gestión de sesiones de un entorno real.

---

## Diseño e Interfaz (Criterios Generales)

La interfaz ha sido desarrollada con **Jetpack Compose** siguiendo las guías de Material Design 3, priorizando la claridad y la accesibilidad.

### Galería de Vistas

| **Acceso Seguro** | **Registro de Usuarios** |
|:---:|:---:|
| <img src="fotos-documentacion/captura_login.png" width="250" alt="Pantalla de Login" /> | <img src="fotos-documentacion/captura_registro.png" width="250" alt="Pantalla de Registro" /> |
| *Inicio de sesión validado.* | *Formulario de creación de cuenta.* |

| **Mis Conversaciones** | **Chat Interactivo** |
|:---:|:---:|
| <img src="fotos-documentacion/captura_home.png" width="250" alt="Pantalla Home" /> | <img src="fotos-documentacion/captura_chats.png" width="250" alt="Pantalla de Chat" /> |
| *Lista de contactos. Un toque vibra el nombre.* | *Envío y recepción. Toca el mensaje para sentirlo.* |

| **Traductor Manual** | **Ajustes de Vibración** |
|:---:|:---:|
| <img src="fotos-documentacion/captura_traductor.png" width="250" alt="Traductor Manual" /> | <img src="fotos-documentacion/captura_ajustes.png" width="250" alt="Ajustes" /> |
| *Playground: Escribe y transmite vibración.* | *Calibración precisa de la velocidad del Morse.* |

### Justificación de Diseño y Accesibilidad

El proyecto sigue el principio de "Design for All" para cubrir necesidades de diversidad funcional:

1.  **Necesidad Social:** Solución para personas con discapacidad visual o sordo-ceguera que requieren privacidad (evitando el uso de TalkBack en público) o comunicación en entornos de silencio absoluto.
2.  **Interfaz de Alto Contraste:** Uso del color Cian (`#4DD0E1`) sobre fondos neutros para maximizar la visibilidad y ayudar en casos de daltonismo.
3.  **Tipografía:** Tamaño base de **22sp** e interlineado de **30sp** para facilitar la lectura.
4.  **Áreas Táctiles:** Padding ampliado a **20dp** en elementos interactivos para usuarios con dificultades motoras.

---

## Evidencias RA5: Gestión de Informes

La aplicación cumple con los criterios de generación de informes integrados mediante la funcionalidad de exportación de chats.

* **Herramienta de generación:** Se ha implementado un sistema nativo en Kotlin que recopila el historial de mensajes de la base de datos Room.
* **Integración:** La funcionalidad es accesible desde el menú de opciones (tres puntos) dentro de cada conversación.
* **Resultado:** Genera un archivo `.txt` en el almacenamiento privado de la aplicación que incluye:
    * Cabecera con fecha y hora de generación.
    * Identificación de los participantes.
    * Historial cronológico de mensajes con marcas de tiempo.

---

## Evidencias RA7: Distribución e Instalación

Estrategia definida para el despliegue y distribución del software:

### Empaquetado y Firma
El proyecto se distribuye mediante un archivo **APK firmado** (`app-release.apk`), generado desde Android Studio mediante Keystore seguro. Esto garantiza la integridad y autoría del software.

### Canales de Distribución
1.  **Repositorio GitHub:** Acceso al código fuente para auditoría y colaboración.
2.  **Distribución Sideloading:** Entrega directa del APK para instalación en dispositivos sin servicios de Google o entornos controlados.

### Instalación Desatendida
Para entornos corporativos o educativos (ej. tablets de una asociación), la aplicación soporta instalación mediante ADB sin requerir interacción del usuario en el primer inicio, ya que no solicita permisos críticos en tiempo de ejecución (Runtime Permissions) bloqueantes.

**Comando de instalación:**
`adb install -r app-release.apk`

---

## Evidencias RA8: Calidad, Seguridad y Rendimiento

### Análisis de Consumo de Recursos (Profiler)
Se ha realizado un perfilado en tiempo real de la aplicación en un entorno Android 14.

<img src="fotos-documentacion/evidencia_profiler.png" width="800" alt="Gráfica de Rendimiento Android Profiler" />

**Resultados:**
* **Memoria (RAM):** Consumo estable entre **113-128 MB**. La gráfica plana demuestra la correcta implementación de `LazyColumn`, reciclando vistas y evitando fugas de memoria.
* **CPU:** Uso cercano al 0% en reposo. Los picos de procesamiento solo ocurren durante la traducción texto-morse y vibración, optimizado mediante Corrutinas (`Dispatchers.IO`).

### Seguridad y Datos
* **Almacenamiento Local:** Uso de Room Database en directorio privado (`/data/data/...`). El aislamiento de procesos de Android (Sandboxing) impide que otras apps lean los mensajes.
* **Operatividad Offline:** Al no realizar conexiones a internet, se eliminan vulnerabilidades de interceptación de datos en tránsito.

### Pruebas de Estrés
La arquitectura está diseñada para soportar grandes volúmenes de datos (ej. +10.000 mensajes) sin bloqueo de la UI, gracias a la carga perezosa de listas y la ejecución de consultas a base de datos en hilos secundarios.

---

## Stack Tecnológico

* **Lenguaje:** Kotlin (100%)
* **UI:** Jetpack Compose
* **Arquitectura:** MVVM (Model-View-ViewModel)
* **Persistencia:** Room Database (SQLite)
* **Asincronía:** Coroutines & Flows
* **Documentación:** KDoc integrado en código fuente.

### Esquema de Base de Datos

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
        string fecha
        long timestamp
    }
