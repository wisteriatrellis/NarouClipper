package com.unlimtech.narouclipper

import android.app.Application

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    companion object {
        lateinit var instance: GlobalApplication
            private set
    }
}