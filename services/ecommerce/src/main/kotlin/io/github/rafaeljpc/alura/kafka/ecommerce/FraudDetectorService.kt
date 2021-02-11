package io.github.rafaeljpc.alura.kafka.ecommerce

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.*
import kotlin.reflect.jvm.jvmName

private val log = KotlinLogging.logger {}

fun main() {
    val consumer = KafkaConsumer<String, String>(properties())

    consumer.subscribe(listOf("ECOMMERCE_NEW_ORDER"))

    while (true) {

        val records = consumer.poll(Duration.ofMillis(100))

        if (records.isEmpty) {
            continue
        }

        records.forEach {
            log.info(
                """
            ------------------------------------------
            Processing New Order
            key = ${it.key()}
            value = ${it.value()}
            partition = ${it.partition()}
            offset = ${it.offset()}
        """.trimIndent()
            )

            Thread.sleep(500)
            log.info("Order processed")
        }
    }
}

private fun properties(): Properties = hashMapOf(
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.jvmName,
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.jvmName,
    ConsumerConfig.GROUP_ID_CONFIG to "FraudDetectorService"
).toProperties()