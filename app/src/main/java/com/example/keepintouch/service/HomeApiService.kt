package com.example.keepintouch.service

import com.example.keepintouch.addPersonSection.Person
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

private const val BASE_URL = "https://keep-in-touch.tk/ "

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface HomeApiService {
    @GET("api/kit/kit-people")
    fun getContacts(@Header("Authorization") authHeader: String): Call<List<Person>>
}

val homeApiService = retrofit.create(HomeApiService::class.java)