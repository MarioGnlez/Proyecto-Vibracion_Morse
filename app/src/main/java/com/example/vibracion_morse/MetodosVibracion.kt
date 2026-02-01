package com.example.vibracion_morse

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

// Función global: Convierte texto normal a puntos y rayas
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

    return texto.uppercase()
        .mapNotNull { char -> morseMap[char] }
        .joinToString(separator = " ")
}

// Función global: Recibe el código morse y hace vibrar el teléfono
fun vibrarPatronMorse(context: Context, codigoMorse: String) {
    if (codigoMorse.isBlank()) return

    val unidadBase = 100L
    val tiempos = mutableListOf<Long>(0)

    for (caracter in codigoMorse) {
        val ultimoIndice = tiempos.lastIndex
        when (caracter) {
            '.' -> {
                tiempos.add(unidadBase)
                tiempos.add(unidadBase)
            }
            '-' -> {
                tiempos.add(3 * unidadBase)
                tiempos.add(unidadBase)
            }
            ' ' -> {
                tiempos[ultimoIndice] += (2 * unidadBase)
            }
            '/' -> {
                tiempos[ultimoIndice] += (6 * unidadBase)
            }
        }
    }

    val patron = tiempos.toLongArray()

    // Lógica de vibración compatible con versiones nuevas y viejas
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        manager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val effect = VibrationEffect.createWaveform(patron, -1)
        vibrator.vibrate(effect)
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(patron, -1)
    }
}