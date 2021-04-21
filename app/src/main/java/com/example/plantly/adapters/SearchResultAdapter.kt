package com.example.plantly.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.plantly.R
import com.example.plantly.activity.PlantDetailsActivity
import com.example.plantly.models.Plant
import com.google.gson.Gson
import kotlinx.android.synthetic.main.plant_search_result.view.*

class SearchResultAdapter(private val plants: Plant) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return plants.data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.plant_search_result, parent, false))
    }

    /*Bind each plant item to a view in the recycler*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thePlant = plants.data[position]

        if (thePlant.commonName == null) {
            holder.name.text = thePlant.scientificName
        } else {
            holder.name.text = thePlant.commonName
        }

        /*Add a click listener to each item that will redirect to plant details activity*/
        holder.name.setOnClickListener {
            val plantIntent = Intent(holder.itemView.context, PlantDetailsActivity::class.java)
            plantIntent.putExtra("plantStr", Gson().toJson(thePlant))
            holder.itemView.context.startActivity(plantIntent)
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val name: Button = view.plant_button
    }
}