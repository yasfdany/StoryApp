package dev.studiocloud.storyapp.utils

import android.text.TextUtils
import android.util.Patterns

open class Tools {
    open fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}