package dev.studiocloud.storyapp

import android.app.Application
import android.content.res.Resources
import android.util.TypedValue
import dev.studiocloud.storyapp.utils.Prefs

class App: Application() {
    companion object {
        var prefs: Prefs? = null
        lateinit var instance: App
            private set

        val Number.toPx get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        prefs = Prefs(applicationContext)
    }
}