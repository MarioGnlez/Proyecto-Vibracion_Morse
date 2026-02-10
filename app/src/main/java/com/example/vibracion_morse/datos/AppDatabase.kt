package com.example.vibracion_morse.datos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vibracion_morse.dao.ChatDao
import com.example.vibracion_morse.dao.MensajeDao
import com.example.vibracion_morse.dao.SeguimientoDao
import com.example.vibracion_morse.dao.UsuarioDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Usuario::class, Chat::class, Mensaje::class, Seguimiento::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun chatDao(): ChatDao
    abstract fun mensajeDao(): MensajeDao
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
                    val dao = database.usuarioDao()
                    if (dao.obtenerUsuario("admin") == null) {
                        dao.registrarUsuario(Usuario(
                            nombreCompleto = "Administrador Clínica",
                            telefono = "000000000",
                            usuario = "admin",
                            contrasena = "admin",
                            esAdmin = true
                        ))
                    }
                    if (dao.obtenerUsuario("paciente1") == null) {
                        dao.registrarUsuario(Usuario(
                            nombreCompleto = "Paciente Uno",
                            telefono = "111111111",
                            usuario = "paciente1",
                            contrasena = "1234",
                            esAdmin = false
                        ))
                    }
                }
            }
        }
    }
}