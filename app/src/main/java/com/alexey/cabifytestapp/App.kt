package com.alexey.cabifytestapp

import android.app.Application

internal class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}