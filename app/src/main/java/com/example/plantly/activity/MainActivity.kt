package com.example.plantly.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.plantly.R

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_toolbar.*

lateinit var sharedPreferences: SharedPreferences
const val themeKey = "currentTheme"

class MainActivity : AppCompatActivity() {
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

        setContentView(R.layout.activity_main)
        setSupportActionBar(app_toolbar)

        /*Set click listeners for buttons*/

        plant_category_btn.setOnClickListener {
            Toast.makeText(applicationContext, "All Plants", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, AllPlantsActivity::class.java))
        }
        my_collection_btn.setOnClickListener {
            Toast.makeText(applicationContext, "My Plants", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MyPlantsActivity::class.java))
        }
        camera_btn.setOnClickListener {
            Toast.makeText(applicationContext, "Camera", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, CameraActivity::class.java))
        }
        settings_btn.setOnClickListener {
            Toast.makeText(applicationContext, "Settings", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
    }

    /*Provide relevant menu on the toolbar*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.searchable_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                searchIntent.putExtra("kw", searchView.query.toString())
                startActivity(searchIntent)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}