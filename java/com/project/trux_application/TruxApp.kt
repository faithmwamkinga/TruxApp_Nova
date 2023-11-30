package com.project.trux_application

import android.app.Application
import android.content.Context

class TruxApp: Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}