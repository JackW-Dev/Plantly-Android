package com.example.plantly.models

import com.google.gson.annotations.SerializedName


data class Plant(val data: List<Data>, val links: Links, val meta: Meta) {
    data class Data(
            val author: String,
            val bibliography: String,
            @SerializedName("common_name")
            val commonName: String?,
            val family: String,
            @SerializedName("family_common_name")
            val familyCommonName: String,
            val genus: String,
            @SerializedName("genus_id")
            val genusId: Int,
            val id: Int,
            @SerializedName("image_url")
            val imageUrl: String,
            val links: Links,
            val rank: String,
            @SerializedName("scientific_name")
            val scientificName: String,
            val slug: String,
            val status: String,
            val synonyms: List<String>,
            val year: Int
    ) {
        data class Links(val genus: String, val plant: String, val self: String)
    }

    data class Links(val first: String, val last: String, val next: String, val self: String)

    data class Meta(val total: Int)
}