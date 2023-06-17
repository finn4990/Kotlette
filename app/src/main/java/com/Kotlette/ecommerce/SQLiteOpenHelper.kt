package com.Kotlette.ecommerce

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Definisci il nome e la versione del database
    companion object {
        private const val DATABASE_NAME = "Ecommerce.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    fun isPassCorrect(email: String, pass: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE email = ? AND password"
        val cursor = db.rawQuery(query, arrayOf(email, pass))

        val passCorrect = cursor.count > 0

        cursor.close()
        db.close()

        return passCorrect
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}
