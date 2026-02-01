package com.example.vibracion_morse.datos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioPropietario: String, // El usuario que ve el chat (TÚ)
    val usuarioContacto: String     // El usuario con el que hablas
)