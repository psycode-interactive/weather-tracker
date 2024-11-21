package com.psycodeinteractive.weathertracker.data.source.remote

import com.psycodeinteractive.weathertracker.data.model.ForecastDataModel
import com.psycodeinteractive.weathertracker.data.model.QueriedCityDataModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApiService {
    @GET("current.json")
    suspend fun fetchForecast(
        @Query("q") query: String
    ): ForecastDataModel

    @GET("search.json")
    suspend fun search(
        @Query("q") query: String
    ): List<QueriedCityDataModel>
}
