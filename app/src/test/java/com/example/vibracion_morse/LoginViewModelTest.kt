package com.example.vibracion_morse

import android.app.Application
import com.example.vibracion_morse.datos.AppDatabase
import com.example.vibracion_morse.datos.Usuario
import com.example.vibracion_morse.dao.UsuarioDao
import com.example.vibracion_morse.ventanas.LoginViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: LoginViewModel
    private lateinit var daoMock: UsuarioDao
    private lateinit var appMock: Application

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // 1. Simulamos (Mockeamos) la App y la Base de Datos
        appMock = mockk(relaxed = true)
        daoMock = mockk(relaxed = true)

        // Truco para simular la base de datos estática
        mockkObject(AppDatabase)
        every { AppDatabase.getDatabase(any()) } returns mockk {
            every { usuarioDao() } returns daoMock
        }

        viewModel = LoginViewModel(appMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al iniciar, los campos estan vacios`() {
        assertEquals("", viewModel.usuario)
        assertEquals("", viewModel.contrasena)
        assertNull(viewModel.error)
    }

    @Test
    fun `login falla si los campos estan vacios`() = runTest {
        viewModel.usuario = ""
        viewModel.contrasena = ""

        viewModel.onAccionClick { }

        // Avanzamos el tiempo de la corrutina
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Usuario y contraseña son obligatorios", viewModel.error)
    }

    @Test
    fun `login exitoso llama al callback`() = runTest {
        // PREPARAR
        val user = "test"
        val pass = "1234"
        viewModel.usuario = user
        viewModel.contrasena = pass

        // Enseñamos al Mock qué devolver cuando le pregunten
        coEvery { daoMock.login(user, pass) } returns Usuario(1, "Test", "111", user, pass)

        var loginExitoso = false

        // EJECUTAR
        viewModel.onAccionClick {
            loginExitoso = true
        }

        testDispatcher.scheduler.advanceUntilIdle()

        // VERIFICAR
        assertTrue(loginExitoso)
        assertNull(viewModel.error)
    }
}