package io.github.rafaeljpc.alura.kafka.ecommerce.common

import io.github.rafaeljpc.alura.kafka.ecommerce.common.GsonDeserializer.Companion.TYPE_CONFIG
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.io.Closeable
import java.time.Duration
import java.util.*
import java.util.regex.Pattern
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}

class KafkaService<T : Any>(
    private val groupId: String,
    private val topic: String? = null,
    private val topicPattern: Pattern? = null,
    private val parse: (ConsumerRecord<String, T>) -> Unit,
    private val type: KClass<T>,
    private val properties: Map<String, String> = emptyMap(),
) : Closeable {

    private val consumer = KafkaConsumer<String, T>(
        properties(type, groupId, properties)
    )

    init {
        if (topic.isNullOrEmpty() && topicPattern == null) {
            error("Topic should not be null")
        }

        if (topic != null && topic.isNotBlank()) {
            consumer.subscribe(listOf(topic))
        } else {
            consumer.subscribe(topicPattern)
        }
    }

    fun run() {
        while (true) {
            val records = consumer.poll(Duration.ofMillis(1000)).toList()
            if (records.isNotEmpty()) {
                logger.info("Encontrei ${records.size} registros")

                records.forEach { parse.invoke(it) }
            }
        }
    }

    private fun properties(
        type: KClass<T>,
        groupId: String,
        overrideProperties: Map<String, String>
    ): Map<String, String> {
        val properties = mutableMapOf<String, String>(
            BOOTSTRAP_SERVERS_CONFIG to "127.0.0.1:9092",
            KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
            VALUE_DESERIALIZER_CLASS_CONFIG to GsonDeserializer::class.java.name,
            GROUP_ID_CONFIG to groupId,
            CLIENT_ID_CONFIG to UUID.randomUUID().toString(),
            TYPE_CONFIG to type::class.java.name,
        )
        properties.putAll(overrideProperties)
        return properties
    }

    override fun close() {
        consumer.close()
    }
}

