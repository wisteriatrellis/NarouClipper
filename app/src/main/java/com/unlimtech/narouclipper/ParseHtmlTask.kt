package com.unlimtech.narouclipper

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

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
        Log.d("myDebug", "before")
        val document: Document = Jsoup
            .connect("https://ncode.syosetu.com/n3808fy/1/")
            .get()

        /*
        例外処理必要
         */
        val content: String = document
            .selectFirst("div#novel_honbun")
            .select("p")
            .joinToString("") {
                "${it.text()}\n"
            }

        Log.d("myDebug", "OK")
        Log.d("myDebug", content)
        return content
    }
}