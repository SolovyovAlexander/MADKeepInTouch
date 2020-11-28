package com.example.keepintouch.addPersonSection

import com.example.keepintouch.service.Regularity
import com.squareup.moshi.Json

data class Person(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "name") val name: String,
    @Json(name = "priority") val priority: Int,
    @Json(name = "regularity") val regularity: Regularity? = null,
    @Json(name = "contact") val contact: String? = ""
)
