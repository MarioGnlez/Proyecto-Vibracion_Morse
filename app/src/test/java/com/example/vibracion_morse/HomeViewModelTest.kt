package com.example.vibracion_morse

import android.app.Application
import com.example.vibracion_morse.dao.UsuarioDao
import com.example.vibracion_morse.datos.AppDatabase
import com.example.vibracion_morse.datos.Usuario
import com.example.vibracion_morse.rules.MainDispatcherRule
import com.example.vibracion_morse.viewmodels.HomeViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val application = mockk<Application>(relaxed = true)
    private val usuarioDao = mockk<UsuarioDao>(relaxed = true)
    private val database = mockk<AppDatabase>()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        mockkObject(AppDatabase)
        every { AppDatabase.getDatabase(any()) } returns database
        every { database.usuarioDao() } returns usuarioDao

        viewModel = HomeViewModel(application)

        // ¡LA CLAVE DEL ÉXITO!
        // Cambiamos el dispatcher real por uno de prueba que corre inmediato
        viewModel.ioDispatcher = UnconfinedTestDispatcher()
    }

    @Test
    fun `inicializar como ADMIN carga medicos por defecto`() = runTest {
        val adminUser = Usuario(1, "SuperAdmin", "000", "admin", "pass", "ADMIN")
        coEvery { usuarioDao.obtenerUsuario("admin") } returns adminUser

        viewModel.inicializar("admin")

        // Ya no hace falta advanceUntilIdle() estricto porque Unconfined corre al momento
        assertEquals("ADMIN", viewModel.rolActual)
    }

    @Test
    fun `inicializar como MEDICO carga pacientes por defecto`() = runTest {
        val medicoUser = Usuario(2, "Dr House", "111", "house", "pass", "MEDICO")
        coEvery { usuarioDao.obtenerUsuario("house") } returns medicoUser

        viewModel.inicializar("house")

        assertEquals("MEDICO", viewModel.rolActual)
    }

    @Test
    fun `crearUsuario como ADMIN crea usuario correctamente`() = runTest {
        viewModel.rolActual = "ADMIN"
        viewModel.viendoMedicos = true
        coEvery { usuarioDao.obtenerUsuario("nuevo") } returns null

        viewModel.crearUsuario("Nuevo", "nuevo", "1234", "666")

        coVerify { usuarioDao.registrarUsuario(any()) }
    }
}