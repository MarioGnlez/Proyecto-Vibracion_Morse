package com.example.vibracion_morse.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy // <--- IMPORTANTE AÑADIR ESTE IMPORT
import androidx.room.Query
import com.example.vibracion_morse.datos.Usuario

@Dao
interface UsuarioDao {

    // CAMBIO AQUÍ: Añadimos onConflict = OnConflictStrategy.IGNORE
    // Esto significa: "Si el usuario ya existe, no hagas nada y no des error".
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registrarUsuario(usuario: Usuario)

    // Para comprobar si el nombre de usuario ya existe al registrarse
    @Query("SELECT * FROM usuarios WHERE usuario = :nombreUsuario LIMIT 1")
    suspend fun obtenerUsuario(nombreUsuario: String): Usuario?

    // Para hacer login: busca coincidencia exacta de usuario Y contraseña
    @Query("SELECT * FROM usuarios WHERE usuario = :nombreUsuario AND contrasena = :pass LIMIT 1")
    suspend fun login(nombreUsuario: String, pass: String): Usuario?
}