package com.example.vibracion_morse.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vibracion_morse.datos.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    // Obtiene los chats del usuario logueado en tiempo real
    @Query("SELECT * FROM chats WHERE usuarioPropietario = :miUsuario")
    fun obtenerMisChats(miUsuario: String): Flow<List<Chat>>

    @Insert
    suspend fun insertarChat(chat: Chat)

    // Verifica si ya tienes un chat con esa persona
    @Query("SELECT * FROM chats WHERE usuarioPropietario = :miUsuario AND usuarioContacto = :contacto LIMIT 1")
    suspend fun existeChat(miUsuario: String, contacto: String): Chat?
}