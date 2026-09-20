package com.example.helia.activity

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object AppSettings {

    private const val PREF_NAME = "HeliaSettings"

    private const val KEY_THEME = "theme"
    private const val KEY_SAVE_USERNAME = "save_username"
    private const val KEY_SAVE_PASSWORD = "save_password"

    // تم‌ها
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"
    const val THEME_SYSTEM = "system"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // -------------------------
    // تم
    // -------------------------

    fun getTheme(context: Context): String {
        return prefs(context).getString(
            KEY_THEME,
            THEME_SYSTEM
        ) ?: THEME_SYSTEM
    }

    fun setTheme(context: Context, theme: String) {
        prefs(context)
            .edit()
            .putString(KEY_THEME, theme)
            .apply()

        when (theme) {
            THEME_LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }

            THEME_DARK -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }

            THEME_SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
            }
        }
    }

    // -------------------------
    // ذخیره نام کاربر
    // -------------------------

    fun isSaveUsername(context: Context): Boolean {
        return prefs(context).getBoolean(
            KEY_SAVE_USERNAME,
            true
        )
    }

    fun setSaveUsername(context: Context, value: Boolean) {
        prefs(context)
            .edit()
            .putBoolean(KEY_SAVE_USERNAME, value)
            .apply()
    }

    // -------------------------
    // ذخیره رمز کاربر
    // -------------------------

    fun isSavePassword(context: Context): Boolean {
        return prefs(context).getBoolean(
            KEY_SAVE_PASSWORD,
            false
        )
    }

    fun setSavePassword(context: Context, value: Boolean) {
        prefs(context)
            .edit()
            .putBoolean(KEY_SAVE_PASSWORD, value)
            .apply()
    }
}