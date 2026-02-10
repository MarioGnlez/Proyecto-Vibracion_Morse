package com.example.vibracion_morse.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vibracion_morse.datos.Seguimiento
import kotlinx.coroutines.flow.Flow

@Dao
interface SeguimientoDao {
    @Query("SELECT * FROM seguimientos WHERE pacienteId = :pacienteId ORDER BY id DESC")
    fun obtenerSeguimientoPorPaciente(pacienteId: String): Flow<List<Seguimiento>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRegistro(seguimiento: Seguimiento)
}