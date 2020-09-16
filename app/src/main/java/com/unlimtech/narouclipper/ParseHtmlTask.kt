package com.unlimtech.narouclipper

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

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
            .connect("https://jsoup.org/apidocs/org/jsoup/select/Selector.html")
            .get()
        Log.d("myDebug", "OK")
        Log.d("myDebug", document.html())
        return document.html()
    }

}