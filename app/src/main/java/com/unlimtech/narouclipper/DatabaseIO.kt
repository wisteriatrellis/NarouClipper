package com.unlimtech.narouclipper

import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask

class DatabaseIO(
    val dbHelper: InputLogDbHelper,
    val inputLog: MutableList<String>
) : AsyncTask<DbOperation, String, Unit>() {
    var readableDB: SQLiteDatabase? = null
    var writableDB: SQLiteDatabase? = null

    override fun doInBackground(vararg params: DbOperation) {
        when (params[0]) {
            DbOperation.READ -> readData()
            DbOperation.WRITE -> writeData()
        }
    }

    fun closeDB() {

    }

    private fun readData() {

    }

    private fun writeData() {

    }
}