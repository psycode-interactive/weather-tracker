package com.psycodeinteractive.weathertracker.data.network.interceptor

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.collections.map

class ErrorInterceptor(
    private val json: Json,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
//        if (response.isSuccessful)
            return response
//
//        val errorBody = response.body?.takeIf { it.contentLength() > 0L }?.string()
//
//        if (errorBody == null) {
//            return response.newBuilder().body(null).build()
//        }
          //TODO: Didn't have time to implement extensive error handling - swallowing all exceptions in repository for now
    }
}
