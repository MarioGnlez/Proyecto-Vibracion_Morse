# 📘 Manual de Usuario - Sistema de Gestión Clínica (SGC)

Bienvenido al manual de usuario de **Morse Chat SGC**. Este documento detalla cómo utilizar la aplicación según su perfil profesional (Administrador o Médico).

> **Nota Importante:** Por motivos de seguridad y privacidad, los **Pacientes** no tienen credenciales de acceso directo a la aplicación. Su interacción es asistida por el personal médico.

---

## 1. Inicio de Sesión
Al abrir la aplicación, verá la pantalla de inicio de sesión.

* Introduzca su **ID de Usuario** y **Contraseña**.
* Pulse el botón **"INICIAR SESIÓN"**.
* *Si introduce credenciales de un paciente, el sistema bloqueará el acceso automáticamente.*

---

## 2. Perfil de Administrador
El administrador tiene control total sobre la gestión del personal y el censo de pacientes.

### 2.1. Navegación Principal
En la parte superior encontrará un selector para cambiar de vista:
* **Botón MÉDICOS:** Muestra el listado de profesionales sanitarios.
* **Botón PACIENTES:** Muestra el listado de todos los pacientes registrados en el centro.

### 2.2. Dar de Alta (Crear Usuario)
1.  Pulse el botón **`+`** situado en la esquina superior derecha.
2.  El formulario se adaptará según la vista en la que esté (Si está viendo Médicos, creará un Médico; si ve Pacientes, creará un Paciente).
3.  Rellene: **Nombre Completo**, **ID de Usuario** (único), **Contraseña** y **Teléfono**.
4.  Pulse **"REGISTRAR"**.

### 2.3. Dar de Baja (Borrar Usuario)
1.  Localice la tarjeta del usuario que desea eliminar.
2.  Pulse el botón rojo con el icono de **Papelera**.
3.  *Advertencia:* Esta acción es irreversible y borrará todo el historial clínico asociado si es un paciente.

---

## 3. Perfil de Médico
El médico dispone de una interfaz simplificada centrada en la atención al paciente y herramientas de comunicación.

### 3.1. Mis Pacientes
Al iniciar sesión, verá directamente el listado de pacientes asignados.
* **Botón HISTORIAL (Naranja):** Accede al expediente clínico del paciente.
* **Botón BORRAR (Rojo):** Permite dar de baja a un paciente de su lista.

### 3.2. Gestión del Historial Clínico
Dentro de la pantalla de Historial de un paciente:
1.  **Ver Evolución:** En la parte inferior verá la lista cronológica de notas y visitas.
2.  **Añadir Nota:**
    * Rellene el nombre del profesional (si es distinto al suyo).
    * Escriba la evolución en el campo de texto grande.
    * Pulse **"GUARDAR REGISTRO"**.
3.  **Exportar Informe:** Pulse el icono de **Descarga/Flecha** en la barra superior para generar un archivo `.txt` con todo el historial.

### 3.3. Herramientas de Comunicación
En la parte inferior de su panel principal, dispone de herramientas asistivas:
* **TRADUCTOR MANUAL:** Abre la utilidad para comunicarse mediante vibraciones (Morse) con pacientes sordoceiegos.
* **AJUSTES:** Configuración de la aplicación.

---

## 4. Solución de Problemas Frecuentes

| Problema | Solución |
| :--- | :--- |
| **"Acceso Denegado" al entrar** | Verifique que no está intentando entrar con una cuenta de rol PACIENTE. |
| **No aparece el teclado** | Pulse sobre el campo de texto. Si persiste, pulse "Atrás" para cerrar el teclado y vuelva a pulsar el campo. |
| **No puedo crear un usuario** | Asegúrese de que todos los campos están rellenos y que el ID de usuario no existe ya en la base de datos. |