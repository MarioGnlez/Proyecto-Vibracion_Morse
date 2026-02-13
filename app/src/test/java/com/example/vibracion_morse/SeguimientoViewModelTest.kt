package com.example.vibracion_morse

import android.app.Application
import com.example.vibracion_morse.dao.SeguimientoDao
import com.example.vibracion_morse.datos.AppDatabase
import com.example.vibracion_morse.viewmodels.SeguimientoViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.vibracion_morse.rules.MainDispatcherRule

@OptIn(ExperimentalCoroutinesApi::class)
class SeguimientoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val application = mockk<Application>(relaxed = true)
    private val seguimientoDao = mockk<SeguimientoDao>(relaxed = true)
    private val database = mockk<AppDatabase>()
    private lateinit var viewModel: SeguimientoViewModel

    @Before
    fun setup() {
        mockkObject(AppDatabase)
        every { AppDatabase.getDatabase(any()) } returns database
        every { database.seguimientoDao() } returns seguimientoDao

        // Simulamos que el flow devuelve una lista vacía por defecto
        every { seguimientoDao.obtenerSeguimientoPorPaciente(any()) } returns flowOf(emptyList())

        viewModel = SeguimientoViewModel(application)
    }

    @Test
    fun `agregarRegistro inserta en DAO y limpia campos`() = runTest {
        // DADO un formulario lleno
        viewModel.fechaSeleccionada = "12/02/2026"
        viewModel.nombreProfesional = "Dr. House"
        viewModel.nuevaNota = "El paciente evoluciona bien."

        // CUANDO agregamos el registro
        viewModel.agregarRegistro("paciente1")

        // ENTONCES verificamos que se llamó al DAO
        coVerify {
            seguimientoDao.insertarRegistro(match {
                it.pacienteId == "paciente1" &&
                        it.empleadoNombre == "Dr. House" &&
                        it.nota == "El paciente evoluciona bien."
            })
        }

        // Y que los campos se limpiaron (nota vacía)
        assertEquals("", viewModel.nuevaNota)
    }
}