package com.example.plantly.interfaces

import com.example.plantly.models.Plant
import retrofit2.Call
import retrofit2.http.*

interface PlantService {
    //Function allowing the user to search for a plant using a keyword
    @GET("plants/search?q=&token=")
    //Adding @Query to the params allows a param to be passed and appended to the URL to create a search request
    fun getPlant(@Query("token") token: String,
                 @Query("q") kw: String): Call<Plant>
}
