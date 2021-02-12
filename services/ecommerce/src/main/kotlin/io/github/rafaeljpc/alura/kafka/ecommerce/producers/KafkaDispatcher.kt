package io.github.rafaeljpc.alura.kafka.ecommerce.producers

import mu.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.StringSerializer
import java.io.Closeable
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.jvm.jvmName

private val log = KotlinLogging.logger {}

class KafkaDispatcher : Closeable {

    private val producer = KafkaProducer<String, String>(properties())

    fun send(topic: String, key: String, value: String) {
        producer.send(ProducerRecord(topic, key, value), producerCB).get()
    }

    private val producerCB = { data: RecordMetadata, e: Exception? ->
        if (e != null) {
            log.error("Error", e)
        } else {
            log.info(
                "${data.topic()}:::${data.partition()}/${data.offset()}@${
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(data.timestamp()), TimeZone.getDefault().toZoneId())
                }"
            )
        }
    }

    private fun properties(): Properties = hashMapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.jvmName,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.jvmName,
    ).toProperties()

    override fun close() {
        producer.close()
    }
}