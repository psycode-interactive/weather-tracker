package com.psycodeinteractive.weathertracker.domain.model

data class QueriedCityDomainModel(
    val id: Int,
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val url: String,
)
