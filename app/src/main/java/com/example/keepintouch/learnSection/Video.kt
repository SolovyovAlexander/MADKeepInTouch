package com.example.keepintouch.learnSection

import com.squareup.moshi.Json

data class Video(
    val name: String,
    val description: String,
    @Json(name = "link")
    val url: String,
    @Json(name = "section")
    val category: Section,
    @Json(name = "image")
    val imgUrl: String?)

data class Section(
    val id: Int,
    val name: String
)