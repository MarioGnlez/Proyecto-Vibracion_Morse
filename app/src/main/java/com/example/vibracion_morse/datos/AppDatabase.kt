package com.example.vibracion_morse.datos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vibracion_morse.dao.SeguimientoDao
import com.example.vibracion_morse.dao.UsuarioDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Usuario::class, Seguimiento::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun seguimientoDao(): SeguimientoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vibracion_morse_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val usuarioDao = database.usuarioDao()

                    if (usuarioDao.obtenerUsuario("admin") == null) {
                        usuarioDao.registrarUsuario(Usuario(
                            nombreCompleto = "Super Administrador",
                            telefono = "000000000",
                            usuario = "admin",
                            contrasena = "admin",
                            rol = "ADMIN"
                        ))
                    }
                    if (usuarioDao.obtenerUsuario("medico1") == null) {
                        usuarioDao.registrarUsuario(Usuario(
                            nombreCompleto = "Dr. House",
                            telefono = "600000000",
                            usuario = "medico1",
                            contrasena = "1234",
                            rol = "MEDICO"
                        ))
                    }
                    if (usuarioDao.obtenerUsuario("paciente1") == null) {
                        usuarioDao.registrarUsuario(Usuario(
                            nombreCompleto = "Juan Paciente",
                            telefono = "611111111",
                            usuario = "paciente1",
                            contrasena = "1234",
                            rol = "PACIENTE"
                        ))
                    }
                }
            }
        }
    }
}