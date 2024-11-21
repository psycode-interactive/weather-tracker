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
        if (response.isSuccessful) return response

        val errorBody = response.body?.takeIf { it.contentLength() > 0L }?.string()

        if (errorBody == null) {
            return response.newBuilder().body(null).build()
        }

        val exception = try {
            IOException(
                json.decodeFromString<List<BackendException>>(errorBody)
                    .map { "${it.field} ${it.message}" }
                    .joinToString("\n")
            )
        } catch (_: Exception) {
            return response.newBuilder().body(null).build()
        }

        throw exception
    }
}