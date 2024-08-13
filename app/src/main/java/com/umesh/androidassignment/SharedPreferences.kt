package com.umesh.androidassignment

import android.content.Context

class SharedPreferences (context: Context) {
        private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        private val editor = sharedPreferences.edit()

    fun saveBearerToken() {
        val token = BuildConfig.TOKEN
        editor.putString("BEARER_TOKEN", token)
            editor.apply()
        }

        fun getBearerToken(): String? {
            return sharedPreferences.getString("BEARER_TOKEN", null)
        }

        fun clearBearerToken() {
            editor.remove("BEARER_TOKEN")
            editor.apply()
        }

}