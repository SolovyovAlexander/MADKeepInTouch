package com.example.keepintouch.service

import com.example.keepintouch.learnSection.Video
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://keep-in-touch.tk/ "

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface LearnApiService {
    @GET("api/kit/lessons")
    fun getLessons(): Call<List<Video>>
}

val learnApiService = retrofit.create(LearnApiService::class.java)