package com.app.huntersclub.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream

class MyDatabaseHelper(private val context: Context) {

    companion object {
        const val DB_NAME = "mhw.db"
    }

    private val dbPath: String
        get() = context.getDatabasePath(DB_NAME).path

    //Checks that database is on correct root, if not copies it from assets
    fun createDatabase() {
        val file = File(dbPath)
        if (!file.exists()) {
            copyDatabaseFromAssets()
        }
    }
    //Installs the db into the app
    private fun copyDatabaseFromAssets() {
        context.assets.open("databases/$DB_NAME").use { input ->
            FileOutputStream(dbPath).use { output ->
                input.copyTo(output)
            }
        }
    }
    //Opens and access the database
    fun openDatabase(): SQLiteDatabase =
        SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
}