package com.example.myapplication.utils

import android.app.Application

object AppUtils {
    private var app: Application? = null

    fun init(application: Application) {
        app = application
    }

    fun getApp(): Application {
        if (app == null) {
            throw IllegalStateException("Utils class is not initialized. Call init() with Application context.")
        }
        return app!!
    }
}