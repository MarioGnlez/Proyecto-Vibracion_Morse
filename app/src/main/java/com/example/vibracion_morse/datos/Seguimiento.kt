package com.example.vibracion_morse.datos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "seguimientos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["usuario"],
            childColumns = ["pacienteId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Seguimiento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pacienteId: String,
    val empleadoNombre: String,
    val fecha: String,
    val nota: String
)