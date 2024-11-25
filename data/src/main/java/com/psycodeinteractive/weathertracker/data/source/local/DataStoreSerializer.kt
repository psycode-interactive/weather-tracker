package com.psycodeinteractive.weathertracker.data.source.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream

// Might be a good idea to encrypt the data
class DataStoreSerializer<Data: @Serializable Any>(
    private val json: Json,
    private val serializer: KSerializer<Data>,
    override val defaultValue: Data
): Serializer<Data> {
    override suspend fun readFrom(input: InputStream): Data {
        try {
            return json.decodeFromStream(serializer, input)
        } catch (e: SerializationException) {
            throw CorruptionException("Cannot read the data.", e)
        }
    }

    override suspend fun writeTo(t: Data, output: OutputStream) {
        json.encodeToStream(serializer, t, output)
    }
}
