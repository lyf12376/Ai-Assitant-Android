package com.example.myapplication.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import com.example.myapplication.Const.ScreenParam

object ScreenConstrainUtils {
    @Composable
    fun GetScreenWidthHeight() {
        val configuration = LocalConfiguration.current
        ScreenParam.screenWidthDp = configuration.screenWidthDp
        ScreenParam.screenHeightDp = configuration.screenHeightDp
    }

    @Composable
    fun GetScreenMetrics() {
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics

        ScreenParam.widthPixels = displayMetrics.widthPixels
        ScreenParam.heightPixels = displayMetrics.heightPixels
        ScreenParam.densityDpi = displayMetrics.densityDpi
        ScreenParam.density = displayMetrics.density
        ScreenParam.xdpi = displayMetrics.xdpi
        ScreenParam.ydpi = displayMetrics.ydpi
    }

    @Composable
    fun getScreenDensity(): Float {
        val density = LocalDensity.current
        return density.density
    }



}