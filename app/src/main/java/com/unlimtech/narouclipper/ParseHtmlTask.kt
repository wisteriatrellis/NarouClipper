
/*
 * jsoup <https://jsoup.org/>
 * Copyright (c) 2009-2020 Jonathan Hedley
 * The MIT License <https://jsoup.org/license>
 */

package com.unlimtech.narouclipper

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception

class ParseHtmlTask : AsyncTask<String, String, String>() {
    var status: String = SUCCESS_TO_CLIP
        private set

    override fun doInBackground(vararg params: String): String {
        val path = params[0]
        Log.d("myDebug", path)
        return parse("$HOST$path")
    }

    private fun parse(url: String) : String{
        val document: Document
        try {
            document = Jsoup
                .connect(url)
                .get()
        } catch (e: Exception) {
            Log.d("myDebug", e.toString())
            this.status = FAILURE_TO_CONNECT
            return this.status
        }

        val content: String
        try {
            content = document.title() + "\n\n" + document
                .selectFirst("div#novel_honbun")
                .select("p")
                .joinToString("") {
                    "${it.text()}\n"
                }
        } catch (e: Exception) {
            Log.d("myDebug", e.toString())
            this.status = FAILURE_TO_READ
            return this.status
        }

        Log.d("myDebug", content)
        return content
    }

    companion object {
        private const val HOST: String = "https://ncode.syosetu.com/"
        private const val FAILURE_TO_CONNECT = "failure to connect to the URL"
        private const val FAILURE_TO_READ = "failure to read the content"
        const val SUCCESS_TO_CLIP = "success to clip the content"
    }
}