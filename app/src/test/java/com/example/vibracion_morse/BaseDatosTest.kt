package com.example.vibracion_morse

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vibracion_morse.dao.UsuarioDao
import com.example.vibracion_morse.datos.AppDatabase
import com.example.vibracion_morse.datos.Usuario
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BaseDatosTest {
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Usamos una base de datos en memoria (se borra al cerrar)
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        usuarioDao = db.usuarioDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun escribirYLeerUsuario() = runBlocking {
        val usuario = Usuario(id = 1, nombreCompleto = "Pepe", telefono = "600", usuario = "pepe", contrasena = "1234")

        usuarioDao.registrarUsuario(usuario)

        val usuarioRecuperado = usuarioDao.obtenerUsuario("pepe")

        assertNotNull(usuarioRecuperado)
        assertEquals("Pepe", usuarioRecuperado?.nombreCompleto)
    }

    @Test
    fun loginFuncionaCorrectamente() = runBlocking {
        val usuario = Usuario(id = 2, nombreCompleto = "Juan", telefono = "700", usuario = "juan", contrasena = "pass")
        usuarioDao.registrarUsuario(usuario)

        // Intento Login correcto
        val loginOk = usuarioDao.login("juan", "pass")
        assertNotNull(loginOk)

        // Intento Login incorrecto
        val loginMal = usuarioDao.login("juan", "incorrecta")
        assertEquals(null, loginMal)
    }
}