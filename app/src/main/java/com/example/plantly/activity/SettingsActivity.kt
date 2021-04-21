package com.example.plantly.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantly.R
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.app_toolbar.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )

        /*Get the themeKey from shared pref to initialise the correct colour scheme*/

        when (sharedPreferences.getString(themeKey, "Default")) {
            "Default" ->  this.setTheme(R.style.Theme_Plantly)
            "Daisy" ->  this.setTheme(R.style.Theme_Plantly_Daisy)
            "Sakura" ->  this.setTheme(R.style.Theme_Plantly_Sakura)
            "Cantaloupe" ->  this.setTheme(R.style.Theme_Plantly_Cantaloupe)
        }

        setContentView(R.layout.activity_settings)
        setSupportActionBar(app_toolbar)

        app_toolbar.title = "Plantly - Settings"

        /*Set up buttons to change the theme - Update shared pref and recreate the activity to apply new theme*/

        default_theme_btn.setOnClickListener {
            sharedPreferences.edit().putString(themeKey, "Default").apply()
            recreate()
        }
        daisy_theme_btn. setOnClickListener {
            sharedPreferences.edit().putString(themeKey, "Daisy").apply()
            recreate()
        }
        sakura_theme_btn.setOnClickListener {
            sharedPreferences.edit().putString(themeKey, "Sakura").apply()
            recreate()
        }
        cantaloupe_theme_btn.setOnClickListener {
            sharedPreferences.edit().putString(themeKey, "Cantaloupe").apply()
            recreate()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}