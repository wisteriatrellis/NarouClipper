package com.unlimtech.narouclipper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clipButton: Button = findViewById<Button>(R.id.clipButton) as Button
        clipButton.setOnClickListener(ClipButtonListener())
    }

    private inner class ClipButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val urlForm: AutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.urlInputTextView) as AutoCompleteTextView
            val inputLog: MutableList<String> = mutableListOf("hello", "world", "hogehoge", "help", "work")
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                applicationContext,
                android.R.layout.simple_dropdown_item_1line,
                inputLog
            )
            urlForm.setAdapter(adapter)

            val parseHtmlTask: ParseHtmlTask = ParseHtmlTask()
            parseHtmlTask.execute(urlForm.text)

            val clipboardManager: ClipboardManager = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData = ClipData.newPlainText(
                "this is the novel content",
                parseHtmlTask.get()
            )
            clipboardManager.setPrimaryClip(clipData)
        }

    }
}