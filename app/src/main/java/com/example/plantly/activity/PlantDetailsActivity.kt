package com.example.plantly.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.plantly.R
import com.example.plantly.models.Plant
import com.example.plantly.network.StorageManager.addToMyPlants
import com.example.plantly.network.StorageManager.getMyPlants
import com.example.plantly.network.StorageManager.removeFromMyPlants
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_plant_details.*
import kotlinx.android.synthetic.main.app_toolbar.*

class PlantDetailsActivity : AppCompatActivity() {
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

        setContentView(R.layout.activity_plant_details)
        setSupportActionBar(app_toolbar)

        /*Get plant extra from previous activity and convert to plant.data object using Gson()*/

        val plantExtra: String = intent.getStringExtra("plantStr")

        val plant: Plant.Data = Gson().fromJson(plantExtra, Plant.Data::class.java)

        /*Check values in plant obj and output to fields accordingly*/

        if (plant.commonName == null) {
            app_toolbar.title = "Plantly - ${plant.scientificName}"
            plant_name_out.text = null
        } else {
            app_toolbar.title = "Plantly - ${plant.commonName}"
            plant_name_out.text = plant.scientificName
        }

        Picasso.get().isLoggingEnabled = true
        Picasso.get()
            .load(plant.imageUrl)
            .placeholder(R.drawable.plant_icon)
            .resize(200, 200)
            .centerInside()
            .into(plant_img)

        family_common_out.text = plant.familyCommonName
        family_latin_out.text = plant.family
        genus_out.text = plant.genus

        /*Set the status of buttons based on if the plant is saved to user library or not*/

        try {
            if (getMyPlants(filesDir).contains(plant)) {
                add_collection_btn.isEnabled = false
                add_collection_btn.text = getString(R.string.in_collection_lbl)
                remove_collection_btn.isEnabled = true
            } else {
                remove_collection_btn.isEnabled = false
            }
        } catch (e: Exception) {
            remove_collection_btn.isEnabled = false
        }

        /*Define click listeners for buttons*/

        add_collection_btn.setOnClickListener {
            addToMyPlants(filesDir, plant)
            Toast.makeText(applicationContext, "Added to collection", Toast.LENGTH_LONG).show()
            recreate()
        }

        remove_collection_btn.setOnClickListener {
            removeFromMyPlants(filesDir, plant)
            Toast.makeText(applicationContext, "Removed from collection", Toast.LENGTH_LONG).show()
            recreate()
        }
    }

    /*Provide relevant menu on the toolbar*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.shareable_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /*Handle menu interaction on the toolbar*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_camera -> {
                val camIntent = Intent(this, CameraActivity::class.java)
                startActivity(camIntent)
                true
            }

            /*Allow the user to share a set string to social media channels*/

            R.id.action_share -> {
                val plantExtra: String = intent.getStringExtra("plantStr")
                val plant: Plant.Data = Gson().fromJson(plantExtra, Plant.Data::class.java)
                val plantShareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    if (plant.commonName == null) {
                        putExtra(Intent.EXTRA_TEXT, "Check out ${plant.scientificName} on Plantly!")
                    } else {
                        putExtra(Intent.EXTRA_TEXT, "Check out ${plant.commonName} on Plantly!")
                    }
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(plantShareIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}