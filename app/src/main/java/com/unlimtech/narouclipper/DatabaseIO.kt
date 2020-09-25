package com.unlimtech.narouclipper

import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.util.Log

class DatabaseIO(
    private val dbOperation: DbOperation,
    private val dbHelper: InputLogDbHelper,
    private val inputLog: MutableList<String>,
    private var db: SQLiteDatabase? = null
) : AsyncTask<Unit, String, Unit>() {

    constructor(other: DatabaseIO) : this(
            other.dbOperation,
            other.dbHelper,
            other.inputLog,
            other.db
        )

    override fun doInBackground(vararg params: Unit) {
        when (dbOperation) {
            DbOperation.READ -> readData()
            DbOperation.WRITE -> writeData()
        }
    }

    fun closeDB() {
        db?.close()
        Log.d("my", "close db")
    }

    private fun readData() {
        if (db?.isOpen != true) {
            db = dbHelper.readableDatabase
            Log.d("my", "new readableDB")
        }
        val cursor = db?.rawQuery("SELECT path FROM input_log;", null)

        cursor?.moveToPosition(-1)
        while (cursor?.moveToNext() == true) {
            inputLog.add(cursor.getString(cursor.getColumnIndex("path")))
            Log.d("my", "read DB " + cursor.getString(cursor.getColumnIndex("path")))
        }
        cursor?.close()
        // readableDB.close()
        Log.d("my", "success onStart")
    }

    private fun writeData() {
        if (db?.isOpen != true) {
            db = dbHelper.writableDatabase
            Log.d("my", "new writableDB")
        }
        db?.execSQL("delete from input_log;")

        inputLog.forEach {
            db?.execSQL("insert into input_log(path) values('$it');")
            Log.d("my", "save DB $it")
        }
        // writableDB.close()
        Log.d("my", "onStop success")
    }
}