package com.example.vibracion_morse.datos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vibracion_morse.dao.ChatDao
import com.example.vibracion_morse.dao.UsuarioDao

// CAMBIO: Version 3
@Database(entities = [Usuario::class, Chat::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun chatDao(): ChatDao

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
                    .fallbackToDestructiveMigration() // Borrará datos antiguos al actualizar
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}