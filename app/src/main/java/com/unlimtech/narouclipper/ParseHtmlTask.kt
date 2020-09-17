package com.unlimtech.narouclipper

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class ParseHtmlTask : AsyncTask<CharSequence, CharSequence, CharSequence>() {

    override fun doInBackground(vararg params: CharSequence): CharSequence {
        val url = StringBuilder(params[0]).toString()
        Log.d("myDebug", url)
        return parse(url)
    }

    override fun onPostExecute(result: CharSequence) {

    }

    private fun parse(url: String) : String{
        Log.d("myDebug", "before")
        val document: Document = Jsoup
            .connect("https://ncode.syosetu.com/n3808fy/1/")
            .get()

        /*
        例外処理必要
         */
        val content = document
            .selectFirst("div#novel_honbun")
            .select("p")
            .joinToString("") {
                it.text() + "\n"
            }

        Log.d("myDebug", "OK")
        Log.d("myDebug", content)
        return content
    }
}