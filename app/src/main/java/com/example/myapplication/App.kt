package com.example.myapplication

import android.app.Application
import com.example.myapplication.utils.AppUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App :Application(){
    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
    }
}