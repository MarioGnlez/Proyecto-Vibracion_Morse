package com.example.vibracion_morse

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

// Aquí guardamos la velocidad de vibración que eliges en Ajustes
object ConfiguracionVibracion {
    var esperaEntreLetras: Long = 300L
    var esperaEntrePalabras: Long = 700L
}

// Esta función convierte texto normal ("HOLA") en puntos y rayas (".... --- .-.. .-")
fun textoAMorse(texto: String): String {
    val morseMap = mapOf(
        'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".",
        'F' to "..-.", 'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---",
        'K' to "-.-", 'L' to ".-..", 'M' to "--", 'N' to "-.", 'O' to "---",
        'P' to ".--.", 'Q' to "--.-", 'R' to ".-.", 'S' to "...", 'T' to "-",
        'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-", 'Y' to "-.--",
        'Z' to "--..",
        '0' to "-----", '1' to ".----", '2' to "..---", '3' to "...--",
        '4' to "....-", '5' to ".....", '6' to "-....", '7' to "--...",
        '8' to "---..", '9' to "----.",
        ' ' to "/", 'Ñ' to "--.--"
    )

    // Convertimos a mayúsculas y vamos letra por letra buscando su traducción
    return texto.uppercase()
        .mapNotNull { char -> morseMap[char] }
        .joinToString(separator = " ")
}

// Esta función coge los puntos y rayas y hace que el móvil vibre
fun vibrarPatronMorse(context: Context, codigoMorse: String) {
    if (codigoMorse.isBlank()) return

    val unidadBase = 100L
    // Lista donde apuntamos cuánto tiempo vibra y cuánto tiempo se calla
    val tiempos = mutableListOf<Long>(0)

    Log.d("vibrarPatronMorse", "Código Morse: $codigoMorse")

    // Recorremos el código morse para calcular los tiempos
    for (caracter in codigoMorse) {
        val ultimoIndice = tiempos.lastIndex
        when (caracter) {
            '.' -> {
                // Un punto es una vibración corta
                tiempos.add(unidadBase)
                tiempos.add(unidadBase)
            }
            '-' -> {
                // Una raya es una vibración larga (el triple que el punto)
                tiempos.add(3 * unidadBase)
                tiempos.add(unidadBase)
            }
            ' ' -> {
                // Espacio entre letras (usa la configuración de ajustes)
                val tiempoActual = tiempos[ultimoIndice]
                val tiempoRestante = ConfiguracionVibracion.esperaEntreLetras - unidadBase
                tiempos[ultimoIndice] = tiempoActual + if(tiempoRestante > 0) tiempoRestante else 0
            }
            '/' -> {
                // Espacio entre palabras (usa la configuración de ajustes)
                val tiempoActual = tiempos[ultimoIndice]
                val tiempoRestante = ConfiguracionVibracion.esperaEntrePalabras - unidadBase
                tiempos[ultimoIndice] = tiempoActual + if(tiempoRestante > 0) tiempoRestante else 0
            }
        }
    }

    val patron = tiempos.toLongArray()

    // Buscamos el motor de vibración del móvil (depende de la versión de Android)
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        manager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    // Mandamos la orden de vibrar
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (patron.isNotEmpty()) {
            val effect = VibrationEffect.createWaveform(patron, -1)
            vibrator.vibrate(effect)
        }
    } else {
        @Suppress("DEPRECATION")
        if (patron.isNotEmpty()) {
            vibrator.vibrate(patron, -1)
        }
    }
}