package com.example.vibracion_morse.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vibracion_morse.datos.Mensaje
import kotlinx.coroutines.flow.Flow

@Dao
interface MensajeDao {
    // Esto busca en la base de datos todos los mensajes entre dos personas y los ordena por fecha
    @Query("""
        SELECT * FROM mensajes 
        WHERE (remitente = :usuario1 AND destinatario = :usuario2) 
           OR (remitente = :usuario2 AND destinatario = :usuario1) 
        ORDER BY timestamp ASC
    """)
    fun obtenerConversacion(usuario1: String, usuario2: String): Flow<List<Mensaje>>

    // Esto guarda un mensaje nuevo en la base de datos cuando le das al botón de enviar
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun enviarMensaje(mensaje: Mensaje)
}