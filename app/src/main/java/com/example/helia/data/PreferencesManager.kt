package com.example.helia.data

import android.content.Context

object PreferencesManager {

    private const val PREF_NAME = "HeliaPrefs"
    private const val USER_ID = "UserID"
    private const val USER_NAME = "UserName"

    private const val PASSWORD = "Password"
    private fun prefs(context: Context) =
        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

    fun saveUser(
        context: Context,
        userID: Int,
        userName: String,
        password: String
    ) {

        prefs(context)
            .edit()
            .putInt(USER_ID, userID)
            .putString(USER_NAME, userName)
            .putString(PASSWORD, password)
            .apply()

    }
    fun getUserID(context: Context): Int {

        return prefs(context)
            .getInt(USER_ID, 0)

    }

    fun getUserName(context: Context): String {
        return prefs(context)
            .getString(USER_NAME, "") ?: ""
    }

    fun getPassword(context: Context): String {

        return prefs(context)
            .getString(PASSWORD, "") ?: ""

    }

    fun clear(context: Context) {

        prefs(context)
            .edit()
            .clear()
            .apply()

    }


}