package com.example.plantly.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Object used to built HTTP requests
object ServiceBuilder {
    private const val URL = "https://trefle.io/api/v1/"
    private val okHttp = OkHttpClient.Builder()

    private val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build())
            .build()

    fun <T> buildService(serviceType: Class<T>): T{
        return retrofit.create(serviceType)
    }
}