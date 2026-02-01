package com.example.vibracion_morse.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vibracion_morse.datos.Mensaje
import kotlinx.coroutines.flow.Flow

@Dao
interface MensajeDao {
    // Obtiene los mensajes entre dos personas (enviados y recibidos) ordenados por fecha
    @Query("""
        SELECT * FROM mensajes 
        WHERE (remitente = :usuario1 AND destinatario = :usuario2) 
           OR (remitente = :usuario2 AND destinatario = :usuario1) 
        ORDER BY timestamp ASC
    """)
    fun obtenerConversacion(usuario1: String, usuario2: String): Flow<List<Mensaje>>

    @Insert
    suspend fun enviarMensaje(mensaje: Mensaje)
}