package com.example.plantly.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantly.R
import com.example.plantly.adapters.MyPlantsAdapter
import com.example.plantly.network.StorageManager.getMyPlants
import kotlinx.android.synthetic.main.activity_my_plants.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.app_toolbar.*

class MyPlantsActivity : AppCompatActivity() {
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

        setContentView(R.layout.activity_my_plants)
        setSupportActionBar(app_toolbar)

        app_toolbar.title = "Plantly - My Plants"

        /*Define stock alert dialog to be used for different messages*/
        val alert: AlertDialog.Builder = AlertDialog.Builder(this@MyPlantsActivity)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setOnDismissListener {
                    onBackPressed()
                }

        try {
            my_plant_recycler.layoutManager = LinearLayoutManager(this@MyPlantsActivity)
            val myPlants = getMyPlants(filesDir)
            if (myPlants.isEmpty()) {
                /*Output a dialog to inform the user that this activity is not currently available
                * This will return the user to the main menu when dismissed*/
                alert.setTitle("Sorry, no can do!")
                    .setMessage("You appear to have no plants in your collection. Come back when you've added a few!")
                    .show()

            } else {
                /*Fill recyclerView with plants from the local file*/
                my_plant_recycler.adapter = MyPlantsAdapter(myPlants)
            }
        } catch (e: Exception) {
            /*Output a dialog to inform the user that this activity is not currently available
                * This will return the user to the main menu when dismissed*/
            alert.setTitle("Sorry, there's an issue right now!")
                .setMessage("You appear to have no plants in your collection or the file has been lost.")
                .show()
        }
    }

    //On restart, recreate and load in users plants.
    //This avoids any already removed plants from still showing in the recyclerview.
    override fun onRestart() {
        super.onRestart()
        recreate()
    }
}