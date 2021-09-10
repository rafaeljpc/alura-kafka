package io.github.rafaeljpc.alura.kafka.ecommerce.common

import com.google.gson.GsonBuilder
import org.apache.kafka.common.serialization.Serializer

class GsonSerializer<T : Any> : Serializer<T> {
    companion object {
        private val gson = GsonBuilder().create()
    }

    override fun serialize(topic: String?, data: T): ByteArray = gson.toJson(data).toByteArray()
}