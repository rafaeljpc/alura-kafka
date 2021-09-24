package io.github.rafaeljpc.alura.kafka.ecommerce.common

import mu.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.io.Closeable
import java.time.Instant
import java.time.format.DateTimeFormatter


private val logger = KotlinLogging.logger {}

class KafkaDispatcher<T> : Closeable {
    companion object {
        private val DT_FORMATTER = DateTimeFormatter.ISO_INSTANT
    }

    private val producer: KafkaProducer<String, T>

    init {
        producer = KafkaProducer(properties())
    }

    private fun properties(): Map<String, Any> = mapOf(
        BOOTSTRAP_SERVERS_CONFIG to "127.0.0.1:9092",
        KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
        VALUE_SERIALIZER_CLASS_CONFIG to GsonSerializer::class.java.name,
        ACKS_CONFIG to "all"
    )

    fun send(topic: String, key: String, value: T) {
        val record = ProducerRecord(topic, key, value)
        producer.send(record) { data, e ->
            if (e != null) {
                logger.error(e) { "Erro ao enviar para o kafka" }
            } else {
                logger.info {
                    """sucesso enviado ${data.topic()}:::partition ${data.partition()}/
                offset ${data.offset()}/ ts ${
                        if (data.hasTimestamp())
                            DT_FORMATTER.format(Instant.ofEpochMilli(data.timestamp()))
                        else "N/A"
                    } """.trimIndent()
                }
            }
        }.get()
    }

    override fun close() {
        producer.close()
    }
}