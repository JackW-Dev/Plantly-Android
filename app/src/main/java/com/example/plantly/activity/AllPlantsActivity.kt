package com.example.plantly.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.example.plantly.R
import kotlinx.android.synthetic.main.activity_all_plants.*
import kotlinx.android.synthetic.main.app_toolbar.*

class AllPlantsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(
                "ThemePref",
                Context.MODE_PRIVATE
        )

        /*Get the themeKey from shared pref to initialise the correct colour scheme*/

        when (sharedPreferences.getString(themeKey, "Default")) {
            "Default" -> this.setTheme(R.style.Theme_Plantly)
            "Daisy" -> this.setTheme(R.style.Theme_Plantly_Daisy)
            "Sakura" -> this.setTheme(R.style.Theme_Plantly_Sakura)
            "Cantaloupe" -> this.setTheme(R.style.Theme_Plantly_Cantaloupe)
        }


        setContentView(R.layout.activity_all_plants)
        setSupportActionBar(app_toolbar)

        app_toolbar.title = "Plantly - All Plants"

        /*Output a dialog to inform the user that this activity is not currently available
        * This will return the user to the main menu when dismissed*/

        AlertDialog.Builder(this@AllPlantsActivity)
            .setTitle("Pardon our rust!")
            .setMessage("This feature is currently unavailable, come back soon!")
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
            .setOnDismissListener {
                onBackPressed()
            }
    }

    /*Provide relevant menu on the toolbar*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.searchable_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchIntent = Intent(this@AllPlantsActivity, SearchActivity::class.java)
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