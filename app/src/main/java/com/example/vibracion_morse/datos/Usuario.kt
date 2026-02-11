package com.example.vibracion_morse.datos

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios", indices = [Index(value = ["usuario"], unique = true)])
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreCompleto: String,
    val telefono: String,
    val usuario: String,
    val contrasena: String,
    val rol: String
)