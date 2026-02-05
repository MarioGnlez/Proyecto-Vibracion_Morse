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

// Aquí definimos las tablas que vamos a usar (Usuarios, Chats y Mensajes) y la versión de la base de datos
@Database(entities = [Usuario::class, Chat::class, Mensaje::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Estas funciones nos sirven para poder guardar y leer datos de cada tabla
    abstract fun usuarioDao(): UsuarioDao
    abstract fun chatDao(): ChatDao
    abstract fun mensajeDao(): MensajeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Esta función se encarga de crear la base de datos si no existe, o devolvernos la que ya hay
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vibracion_morse_database"
                )
                    // Si cambiamos algo en la base de datos, borra la vieja y crea una nueva limpia
                    .fallbackToDestructiveMigration()
                    // Añadimos esto para crear datos de prueba al iniciar la app por primera vez
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Esta clase sirve para meter automáticamente dos usuarios de prueba cuando instalas la app
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = database.usuarioDao()
                    // Si no existe el usuario1, lo creamos
                    if (dao.obtenerUsuario("usuario1") == null) {
                        dao.registrarUsuario(Usuario(
                            nombreCompleto = "Usuario Uno Prueba",
                            telefono = "111111111",
                            usuario = "usuario1",
                            contrasena = "1234"
                        ))
                    }
                    // Si no existe el usuario2, lo creamos
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