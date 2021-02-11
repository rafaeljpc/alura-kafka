package io.github.rafaeljpc.alura.kafka.ecommerce

import mu.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.jvm.jvmName

private val log = KotlinLogging.logger {}

fun main() {
    val producer = KafkaProducer<String, String>(properties())

    val value = "1,2,3000"

    producer.send(
        ProducerRecord("ECOMMERCE_NEW_ORDER", value, value)
    ) { data, e ->
        if (e != null) {
            log.error("Error", e)
            return@send
        }

        log.info(
            "${data.topic()}:::${data.partition()}/${data.offset()}@${
                LocalDateTime.ofInstant(Instant.ofEpochMilli(data.timestamp()), TimeZone.getDefault().toZoneId())
            }"
        )
    }.get()
}

private fun properties(): Properties = hashMapOf(
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.jvmName,
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.jvmName,
).toProperties()
