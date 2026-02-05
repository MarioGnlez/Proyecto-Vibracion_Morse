package com.example.vibracion_morse.datos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mensajes")
data class Mensaje(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val remitente: String,
    val destinatario: String,
    val texto: String,
    val fecha: String,
    val timestamp: Long = System.currentTimeMillis()
)