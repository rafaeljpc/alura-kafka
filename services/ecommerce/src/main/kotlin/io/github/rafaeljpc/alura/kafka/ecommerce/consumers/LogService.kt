package io.github.rafaeljpc.alura.kafka.ecommerce.consumers

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.*
import kotlin.reflect.jvm.jvmName

private val log = KotlinLogging.logger {}

fun main() {
    val consumer = KafkaConsumer<String, String>(consumerProperties())

    consumer.subscribe(Regex("ECOMMERCE.*").toPattern())

    while (true) {

        val records = consumer.poll(Duration.ofMillis(100))

        if (records.isEmpty) {
            continue
        }

        records.forEach {
            log.info(
                """
            ------------------------------------------
            LOG
            topic = ${it.topic()}
            key = ${it.key()}
            value = ${it.value()}
            partition = ${it.partition()}
            offset = ${it.offset()}
        """.trimIndent()
            )
        }
    }
}

private fun consumerProperties(): Properties = hashMapOf(
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.jvmName,
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.jvmName,
    ConsumerConfig.GROUP_ID_CONFIG to "LogService"
).toProperties()