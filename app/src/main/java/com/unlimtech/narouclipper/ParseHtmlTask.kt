package com.unlimtech.narouclipper

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.lang.Exception

class ParseHtmlTask : AsyncTask<String, String, String>() {
    private val host: String = "https://ncode.syosetu.com/"

    override fun doInBackground(vararg params: String): String {
        val path = params[0]
        Log.d("myDebug", path)
        return parse("$host$path")
    }

    override fun onPostExecute(result: String) {

    }

    private fun parse(url: String) : String{
        val document: Document
        try {
            document = Jsoup
                .connect(url)
                .get()
        } catch (e: Exception) {
            /*Toast
                .makeText(
                    context,
                    "",
                    Toast.LENGTH_LONG
                )
                .show()*/
            Log.d("myDebug", "fail to connect to the URL")
            Log.d("myDebug", e.toString())
            return ""
        }

        val content: String
        try {
            content = document
                .selectFirst("div#novel_honbun")
                .select("p")
                .joinToString("") {
                    "${it.text()}\n"
                }
        } catch (e: Exception) {
            Log.d("myDebug", "fail to read the content")
            Log.d("myDebug", e.toString())
            return ""
        }

        // clipdataのあとに移動したい
        Log.d("myDebug", "success to clip the content")
        Log.d("myDebug", content)
        return content
    }
}