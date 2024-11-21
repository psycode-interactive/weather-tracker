package com.psycodeinteractive.weathertracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDataModel(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerialName("tz_id") val timezoneId: String,
    @SerialName("localtime_epoch") val localtimeEpoch: Int,
    val localtime: String
)
