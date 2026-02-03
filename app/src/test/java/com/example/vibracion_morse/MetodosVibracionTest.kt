package com.example.vibracion_morse

import org.junit.Test
import org.junit.Assert.*

/**
 * Pruebas unitarias locales para la lógica de traducción a Morse.
 * Estas pruebas se ejecutan en la JVM de tu ordenador (son rapidísimas).
 */
class MetodosVibracionTest {

    @Test
    fun `traduccion basica SOS funciona correctamente`() {
        val input = "SOS"
        val expected = "... --- ..."
        // Nota: Según tu código, usas joinToString(" ") así que habrá espacios entre letras

        val result = textoAMorse(input)
        assertEquals(expected, result)
    }

    @Test
    fun `traduccion minusculas se convierten a mayusculas`() {
        val input = "hola"
        // H(....) O(---) L(.-..) A(.-)
        val expected = ".... --- .-.. .-"

        val result = textoAMorse(input)
        assertEquals(expected, result)
    }

    @Test
    fun `espacios entre palabras se convierten en barra`() {
        val input = "A B"
        // A(.-) ESPACIO(/) B(-...)
        // Tu código añade un espacio extra por el joinToString: ".- / -..."
        val expected = ".- / -..."

        val result = textoAMorse(input)
        assertEquals(expected, result)
    }

    @Test
    fun `numeros se traducen correctamente`() {
        val input = "123"
        // 1(.----) 2(..---) 3(...--)
        val expected = ".---- ..--- ...--"

        val result = textoAMorse(input)
        assertEquals(expected, result)
    }

    @Test
    fun `caracteres desconocidos se ignoran`() {
        // El @ no está en tu mapa, debería ignorarlo y traducir solo A y B
        val input = "A@B"
        val expected = ".- -..."

        val result = textoAMorse(input)
        assertEquals(expected, result)
    }

    @Test
    fun `texto vacio devuelve cadena vacia`() {
        val input = ""
        val expected = ""

        val result = textoAMorse(input)
        assertEquals(expected, result)
    }

    @Test
    fun `la ñ se traduce correctamente`() {
        val input = "Ñ"
        val expected = "--.--"

        val result = textoAMorse(input)
        assertEquals(expected, result)
    }
}