package com.app.huntersclub.data.database

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.edit

/**
 * Class to manage the local SQL database of the app
 *
 */
class MyDatabaseHelper(private val context: Context) {

    companion object {
        const val DB_NAME = "mhw.db"
        const val DB_VERSION = 1
        private const val PREFS_NAME = "db_prefs"
        private const val KEY_DB_VERSION = "db_version"
    }

    private val dbPath: String
        get() = context.getDatabasePath(DB_NAME).path

    private val prefs: SharedPreferences
        get() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Checks that database is on correct root, if not copies it from assets
     *
     */
    fun createDatabase() {
        val currentVersion = prefs.getInt(KEY_DB_VERSION, 0)
        val dbFile = File(dbPath)

        val needsCopy = !dbFile.exists() || currentVersion < DB_VERSION

        if (needsCopy) {
            copyDatabaseFromAssets()
            prefs.edit { putInt(KEY_DB_VERSION, DB_VERSION) }
        }
    }
    /**
     * Installs the db into the app
     *
     */
    private fun copyDatabaseFromAssets() {
        val dbFile = File(dbPath)
        dbFile.parentFile?.mkdirs()

        context.assets.open("databases/$DB_NAME").use { input ->
            FileOutputStream(dbPath).use { output ->
                input.copyTo(output)
            }
        }
    }
    /**
     * Opens and access the database
     *
     */
    fun openDatabase(): SQLiteDatabase =
        SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
}