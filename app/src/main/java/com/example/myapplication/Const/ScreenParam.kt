package com.example.myapplication.Const

import android.util.Log

object ScreenParam {
    var screenWidthDp = 0
    var screenHeightDp = 0
    var widthPixels = 0
    var heightPixels = 0
    var densityDpi = 0
    var density = 0f
    var xdpi = 0f
    var ydpi = 0f

    fun logAll(){
        Log.d("TAG", "logAll: screenWidthDp $screenHeightDp screenHeightDp $screenHeightDp widthPixels $widthPixels heightPixels $heightPixels densityDpi $densityDpi density $density xdpi $xdpi ydpi $ydpi")
    }
}