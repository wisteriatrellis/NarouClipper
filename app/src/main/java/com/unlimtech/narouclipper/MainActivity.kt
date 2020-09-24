package com.unlimtech.narouclipper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    private val dbHelper: InputLogDbHelper = InputLogDbHelper(this)
    private val inputLog: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val urlForm: AutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.urlInputTextView) as AutoCompleteTextView
        val adapter: ArrayAdapter<String> = createAdapter()
        urlForm.setAdapter(adapter)
        urlForm.threshold = 1

        val clipButton: Button = findViewById<Button>(R.id.clipButton) as Button
        clipButton.setOnClickListener(ClipButtonListener())
    }

    override fun onStart() {
        super.onStart()

        if (inputLog.isNotEmpty()) {
            return
        }

        val readableDB = dbHelper.readableDatabase
        val cursor = readableDB.rawQuery("SELECT path FROM input_log;", null)

        cursor.moveToPosition(-1)
        while (cursor.moveToNext()) {
            inputLog.add(cursor.getString(cursor.getColumnIndex("path")))
            Log.d("my", "read DB " + cursor.getString(cursor.getColumnIndex("path")))
        }
        cursor.close()
        readableDB.close()
        Log.d("my", "success onStart")
    }

    override fun onStop() {
        val writableDB = dbHelper.writableDatabase
        writableDB.execSQL("delete from input_log;")

        inputLog.forEach {
            writableDB.execSQL("insert into input_log(path) values('$it');")
            Log.d("my", "save DB $it")
        }
        writableDB.close()
        Log.d("my", "onStop success")
        super.onStop()
    }

    private fun createAdapter(path: String = ""): ArrayAdapter<String> {
        if (path.isNotEmpty()) {
            inputLog.remove(path)
            inputLog.add(0, path)
        }
        return ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            inputLog
        )
    }

    private inner class ClipButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val urlForm: AutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.urlInputTextView) as AutoCompleteTextView
            val path: String = StringBuilder(urlForm.text).toString()

            val parseHtmlTask: ParseHtmlTask = ParseHtmlTask()
            parseHtmlTask.execute(path)

            val adapter: ArrayAdapter<String> = createAdapter(path)
            urlForm.setAdapter(adapter)

            val resultOfParse: String = parseHtmlTask.get()
            val clipboardManager: ClipboardManager = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData = ClipData.newPlainText(
                "this is the novel content",
                resultOfParse
            )
            clipboardManager.setPrimaryClip(clipData)

            Toast
                .makeText(
                    GlobalApplication
                        .instance
                        .applicationContext,
                    parseHtmlTask.status,
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }
}