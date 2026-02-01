package com.example.vibracion_morse.datos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vibracion_morse.dao.ChatDao
import com.example.vibracion_morse.dao.MensajeDao
import com.example.vibracion_morse.dao.UsuarioDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Usuario::class, Chat::class, Mensaje::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun chatDao(): ChatDao
    abstract fun mensajeDao(): MensajeDao

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
                    if (dao.obtenerUsuario("usuario1") == null) {
                        dao.registrarUsuario(Usuario(
                            nombreCompleto = "Usuario Uno Prueba",
                            telefono = "111111111",
                            usuario = "usuario1",
                            contrasena = "1234"
                        ))
                    }
                    if (dao.obtenerUsuario("usuario2") == null) {
                        dao.registrarUsuario(Usuario(
                            nombreCompleto = "Usuario Dos Prueba",
                            telefono = "222222222",
                            usuario = "usuario2",
                            contrasena = "1234"
                        ))
                    }
                }
            }
        }
    }
}