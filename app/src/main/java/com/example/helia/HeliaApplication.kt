package com.example.helia

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.helia.activity.AppSettings

class HeliaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        applySavedTheme()
    }

    private fun applySavedTheme() {

        when (AppSettings.getTheme(this)) {

            AppSettings.THEME_LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }

            AppSettings.THEME_DARK -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }

            AppSettings.THEME_SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
            }
        }
    }
}