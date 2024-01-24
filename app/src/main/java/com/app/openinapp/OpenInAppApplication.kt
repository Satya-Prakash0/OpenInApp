package com.app.openinapp

import android.app.Application
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OpenInAppApplication:Application() {
    override fun onCreate() {
        super.onCreate()
    }
}