package com.example.keepintouch.service

import com.squareup.moshi.Json

data class Regularity(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "week_days") val week_days: List<String>? = null,
    @Json(name = "notification_type") val notification_type: String,
    @Json(name = "times_a_week") val times_a_week: Int? = null,
    @Json(name = "time_of_month") val time_of_month: String? = null,
    @Json(name = "reminder") val reminder: String? = null
)