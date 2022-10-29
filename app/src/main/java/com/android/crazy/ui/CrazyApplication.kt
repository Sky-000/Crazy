package com.android.crazy.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CrazyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}