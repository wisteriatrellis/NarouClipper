package com.unlimtech.narouclipper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clipButton: Button = findViewById<Button>(R.id.clipButton) as Button
        clipButton.setOnClickListener(ClipButtonListener())
    }

    private inner class ClipButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val myText: TextView = findViewById<TextView>(R.id.textView) as TextView
            myText.text = "change!"

            val clipboardManager: ClipboardManager = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(ClipData.newPlainText("this is the novel content", myText.text))
        }
    }
}