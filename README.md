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
3.  **Tipografía:** Tamaño base de **22sp** e interlineado de **30sp** en el chat para facilitar la lectura.
4.  **Áreas Táctiles:** Padding ampliado a **20dp** en elementos interactivos para usuarios con dificultades motoras.

---

## Evidencias RA5: Gestión de Informes

La aplicación cumple con los criterios de generación de informes integrados. Se ha desarrollado un módulo específico que permite exportar el historial completo de cualquier conversación a un archivo de texto legible.

**Implementación Técnica:**
El siguiente fragmento muestra la función encargada de recopilar los datos de la base de datos, formatearlos con marcas de tiempo y escribir el archivo en el almacenamiento privado del dispositivo.

```kotlin
// Del archivo PantallaChat.kt incluir aqui de la linea 188 a la linea 215
