package com.example.plantly.network

import com.example.plantly.models.Plant
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream

//Object to handle interactions with storage
object StorageManager {

    /*Go through a local file and parse each line into a plant data object and append to an array*/

    fun getMyPlants(fileDirectory: File): ArrayList<Plant.Data> {
        val myPlantArr: ArrayList<Plant.Data> = ArrayList()
        val file = FileInputStream(File(File(fileDirectory, "PlantlyData"), "MyPlants.txt"))
        file.bufferedReader().forEachLine {
            val thisPlant = Gson().fromJson(it, Plant.Data::class.java)
            myPlantArr.add(thisPlant)
        }
        return myPlantArr
    }

    /*Open or create a file and append the current plant as a json string*/

    fun addToMyPlants(fileDirectory: File, plant: Plant.Data) {
        val dir = File(fileDirectory, "PlantlyData")
        dir.mkdirs()
        val file = File(dir, "MyPlants.txt")
        file.appendText(Gson().toJson(plant) + "\n")
    }

    /*Open the plant file and parse all results into an array.
    * Clear the file and remove the unwanted plant from the array.
    * Rewrite array as json strings to the file once unwanted plant has been removed.*/

    fun removeFromMyPlants(fileDirectory: File, plant: Plant.Data) {
        val dir = File(fileDirectory, "PlantlyData")
        dir.mkdirs()
        val file = File(dir, "MyPlants.txt")

        val myPlantArr: ArrayList<Plant.Data> = getMyPlants(fileDirectory)

        for (i in 0 until myPlantArr.size) {
            file.writeText("")
        }

        myPlantArr.remove(plant)

        for (i in 0 until myPlantArr.size) {
            file.appendText(Gson().toJson(myPlantArr[i]) + "\n")
        }
    }
}