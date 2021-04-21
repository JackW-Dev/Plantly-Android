package com.example.plantly.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantly.*
import com.example.plantly.adapters.SearchResultAdapter
import com.example.plantly.interfaces.PlantService
import com.example.plantly.models.Plant
import com.example.plantly.network.ServiceBuilder
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.app_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
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

        setContentView(R.layout.activity_search)
        setSupportActionBar(app_toolbar)

        val query: String = intent.getStringExtra("kw")
        searchPlants(query)
        app_toolbar.title = "Plantly - Search: $query"
    }

    /*Function to search and process results*/
    private fun searchPlants(kw: String){
        val token = "C-Me8dC6rB9cEb8EyxPP4UGMuQ4wRkFzBEFJoqi_NQk" //API Token - Needed for all API calls

        /*Set up variables needed to search the API*/

        val service = ServiceBuilder.buildService(PlantService::class.java)

        val requestCall = service.getPlant(token, kw)

        requestCall.enqueue(object: Callback<Plant> {

            /*Define stock alert dialog to be used for different messages*/
            val alert: AlertDialog.Builder = AlertDialog.Builder(this@SearchActivity)
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setOnDismissListener {
                        onBackPressed()
                    }

            override fun onResponse(call: Call<Plant>, response: Response<Plant>) {

                /*Handle response cases*/
                if (response.isSuccessful) {
                    result_recycler.layoutManager = LinearLayoutManager(this@SearchActivity)

                    if (response.body()!!.meta.total == 0) {

                        /*Output an alert with the following details as no match was found*/

                        alert.setTitle("Sorry, no luck!")
                                .setMessage("We couldn't find anything matching your search in the Trefle database, sorry!")
                                .show()
                    } else {

                        /*Output search results in recyclerView*/

                        result_recycler.adapter = SearchResultAdapter(response.body()!!)
                    }
                } else {
                    /*Output an alert that there was an issue with the API*/
                   alert.setTitle("API error!")
                           .setMessage("We got a reply but it wasn't what we expected. Please try again later or if the issue persists, get in touch!")
                           .show()
                }
            }
            override fun onFailure(call: Call<Plant>, t: Throwable){
                /*Output an alert that there was an issue with the API*/
               alert.setTitle("API error!")
                       .setMessage("We tried to search but got no reply. Please check your network connection and try again!")
                       .show()
            }
        })
    }
}