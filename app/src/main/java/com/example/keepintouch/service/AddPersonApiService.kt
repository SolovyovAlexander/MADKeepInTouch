package com.example.keepintouch.service

import com.example.keepintouch.addPersonSection.Person
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://keep-in-touch.tk/ "

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AddPersonApiService {
    @POST("api/kit/kit-people/")
    fun addPerson(@Header("Authorization") token: String, @Body person: Person): Call<Person>

    @PUT("api/kit/kit-people/{personId}")
    fun putPerson(@Header("Authorization") token: String, @Path("personId") personId: Int, @Body person: Person): Call<Person>

    @GET("api/kit/kit-people/{personId}")
    fun getPerson(@Header("Authorization") token: String, @Path("personId") personId: Int): Call<Person>
}

val addPersonApiService = retrofit.create(AddPersonApiService::class.java)
