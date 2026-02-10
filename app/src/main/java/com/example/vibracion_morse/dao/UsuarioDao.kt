package com.example.vibracion_morse.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vibracion_morse.datos.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registrarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE usuario = :nombreUsuario AND contrasena = :pass LIMIT 1")
    suspend fun login(nombreUsuario: String, pass: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE usuario = :nombreUsuario LIMIT 1")
    suspend fun obtenerUsuario(nombreUsuario: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE esAdmin = 0")
    suspend fun obtenerTodosLosPacientes(): List<Usuario>

    @Query("DELETE FROM usuarios WHERE usuario = :nombreUsuario")
    suspend fun borrarUsuario(nombreUsuario: String)
}