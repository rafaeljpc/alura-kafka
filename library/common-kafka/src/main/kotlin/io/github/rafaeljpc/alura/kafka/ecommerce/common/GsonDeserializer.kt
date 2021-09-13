package io.github.rafaeljpc.alura.kafka.ecommerce.common

import com.google.gson.GsonBuilder
import org.apache.kafka.common.serialization.Deserializer

class GsonDeserializer<T : Any> : Deserializer<T> {
    companion object {
        @JvmStatic
        val TYPE_CONFIG = "io.github.rafaeljpc.alura.kafka.ecommerce.type_config"

        private val gson = GsonBuilder().create()
    }

    private lateinit var type: Class<T>

    override fun configure(configs: MutableMap<String, *>, isKey: Boolean) {
        super.configure(configs, isKey)
        val typeName = configs[TYPE_CONFIG]!!.toString()

        try {
            @Suppress("UNCHECKED_CAST")
            type = Class.forName(typeName) as Class<T>
        } catch (e: ClassCastException) {
            throw RuntimeException("Type for deserialization does not exist in the classpath", e)
        }
    }

    override fun deserialize(topic: String?, data: ByteArray?): T = gson.fromJson(String(data!!), type)
}