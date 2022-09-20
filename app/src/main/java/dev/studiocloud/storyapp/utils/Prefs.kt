package dev.studiocloud.storyapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dev.studiocloud.storyapp.data.source.network.model.LoginResult
import dev.studiocloud.storyapp.data.source.network.model.StoryItem

@SuppressWarnings("UnusedReturnValue")
class Prefs (context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)
    private val gson: Gson = Gson()

    var stories: MutableList<StoryItem> = mutableListOf()
        get() {
            val arrayJson: MutableSet<String>? = preferences.getStringSet(STORIES, mutableSetOf())
            val data: ArrayList<StoryItem> = ArrayList()
            if(arrayJson != null){
                for (item in arrayJson){
                    data.add(gson.fromJson(item, StoryItem::class.java))
                }
            }
            return data
        }
        set(data){
            val arrayJson: MutableSet<String> = mutableSetOf()
            for(item in data){
                arrayJson.add(gson.toJson(item))
            }
            preferences.edit().putStringSet(STORIES, arrayJson).apply()
            field = data
        }

    var user: LoginResult? = null
        get() {
            val userJson: String? = preferences.getString(USER, "")
            var user: LoginResult? = null

            if(userJson != null){
                user = gson.fromJson(userJson, LoginResult::class.java)
            }
            return user
        }
        set(value) {
            val userJson: String? = gson.toJson(value)
            if(userJson != null){
                preferences.edit().putString(USER, userJson).apply()
            }
            field = value
        }

    companion object {
        private const val APP_PREF_NAME = "APP_PREF_NAME"
        private const val USER = "USER"
        private const val STORIES = "STORIES"
    }
}