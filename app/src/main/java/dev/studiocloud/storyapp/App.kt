package dev.studiocloud.storyapp

import android.app.Application
import dev.studiocloud.storyapp.utils.Prefs

class App: Application() {
    companion object {
        var prefs: Prefs? = null
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        prefs = Prefs(applicationContext)
    }
}