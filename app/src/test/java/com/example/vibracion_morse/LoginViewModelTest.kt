package com.example.vibracion_morse

import android.app.Application
import com.example.vibracion_morse.dao.UsuarioDao
import com.example.vibracion_morse.datos.AppDatabase
import com.example.vibracion_morse.datos.Usuario
import com.example.vibracion_morse.rules.MainDispatcherRule
import com.example.vibracion_morse.viewmodels.LoginViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val application = mockk<Application>(relaxed = true)
    private val usuarioDao = mockk<UsuarioDao>()
    private val database = mockk<AppDatabase>()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        mockkObject(AppDatabase)
        every { AppDatabase.getDatabase(any()) } returns database
        every { database.usuarioDao() } returns usuarioDao

        viewModel = LoginViewModel(application)

        // ¡LA CLAVE DEL ÉXITO!
        viewModel.ioDispatcher = UnconfinedTestDispatcher()
    }

    @Test
    fun `login permite acceso a ADMIN`() = runTest {
        val adminUser = Usuario(1, "Admin", "000", "admin", "1234", "ADMIN")
        coEvery { usuarioDao.login("admin", "1234") } returns adminUser

        viewModel.usuario = "admin"
        viewModel.contrasena = "1234"

        var usuarioLogueado: String? = null

        viewModel.login { usuario ->
            usuarioLogueado = usuario
        }

        assertEquals("admin", usuarioLogueado)
        assertNull(viewModel.error)
    }

    @Test
    fun `login BLOQUEA acceso a PACIENTE`() = runTest {
        val pacienteUser = Usuario(2, "Juan", "666", "paciente1", "1234", "PACIENTE")
        coEvery { usuarioDao.login("paciente1", "1234") } returns pacienteUser

        viewModel.usuario = "paciente1"
        viewModel.contrasena = "1234"

        var loginExitoso = false

        viewModel.login {
            loginExitoso = true
        }

        assertEquals(false, loginExitoso)
        assertEquals("Acceso denegado: Los pacientes no tienen acceso a la plataforma.", viewModel.error)
    }
}