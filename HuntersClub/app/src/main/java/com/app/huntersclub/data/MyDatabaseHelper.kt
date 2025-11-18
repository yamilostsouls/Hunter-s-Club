package com.app.huntersclub.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream

class MyDatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "mhw.db"
        const val DB_VERSION = 1
    }

    private val dbPath: String
        get() = context.getDatabasePath(DB_NAME).path

    // Local database exists, no need to create a new one but method is required to function
    override fun onCreate(db: SQLiteDatabase?) {
    }

    // We won't upgrade the database but method is required to function
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    /**
     * Copiar la base de datos desde assets si no existe.
     */
    fun createDatabaseIfNeeded() {
        val file = File(dbPath)

        if (!file.exists()) {
            writableDatabase.close() // Crea carpeta /databases

            copyDatabaseFromAssets()
        }
    }

    private fun copyDatabaseFromAssets() {
        // Como tu archivo está en app/src/main/assets/databases/mhw.db
        context.assets.open("databases/$DB_NAME").use { input ->
            FileOutputStream(dbPath).use { output ->
                input.copyTo(output)
            }
        }
    }

    /**
     * Abre la base de datos y la retorna.
     */
    fun openDatabase(): SQLiteDatabase {
        return SQLiteDatabase.openDatabase(
            dbPath,
            null,
            SQLiteDatabase.OPEN_READWRITE
        )
    }
}