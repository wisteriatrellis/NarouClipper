package com.unlimtech.narouclipper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    private val dbHelper: InputLogDbHelper = InputLogDbHelper(this)
    private val inputLog: MutableList<String> = mutableListOf()
    private var readDatabase: DatabaseIO = DatabaseIO(DbOperation.READ, dbHelper, inputLog)
    private var writeDatabase: DatabaseIO = DatabaseIO(DbOperation.WRITE, dbHelper, inputLog)

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
        readDatabase = DatabaseIO(readDatabase)
        readDatabase.execute()
    }

    override fun onStop() {
        writeDatabase = DatabaseIO(writeDatabase)
        writeDatabase.execute()
        super.onStop()
    }

    override fun onDestroy() {
        readDatabase.closeDB()
        writeDatabase.closeDB()
        Log.d("my", "onDestroy success")
        super.onDestroy()
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

            if (parseHtmlTask.status == ParseHtmlTask.SUCCESS_TO_CLIP) {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.setClassName(
                    "com.ktix007.talk",
                    "com.ktix007.talk.Navigation.MainActivity"
                )
                startActivity(intent)
            }
        }
    }
}