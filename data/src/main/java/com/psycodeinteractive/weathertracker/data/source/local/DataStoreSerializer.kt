package com.psycodeinteractive.weathertracker.data.source.local

import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

// Might be a good idea to encrypt the data
class DataStoreSerializer<Data: @Serializable Any>(
    private val json: Json,
    private val serializer: KSerializer<Data>,
    override val defaultValue: Data
): Serializer<Data> {
    override suspend fun readFrom(input: InputStream): Data {
        return json.decodeFromString(serializer, input.reader().readText())
    }

    override suspend fun writeTo(t: Data, output: OutputStream) {
        output.writer().write(json.encodeToString(serializer, t))
    }
}
