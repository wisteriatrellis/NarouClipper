package com.unlimtech.narouclipper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class InputLogDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USER_LOG)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_USER_LOG)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "InputLog.db"
        const val SQL_CREATE_USER_LOG = "CREATE TABLE IF NOT EXISTS input_log (path TEXT PRIMARY KEY);"
        const val SQL_DELETE_USER_LOG = "DROP TABLE IF EXISTS input_log"
    }
}