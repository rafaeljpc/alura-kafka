package io.github.rafaeljpc.alura.kafka.ecommerce.consumers

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.io.Closeable
import java.time.Duration
import java.util.*
import kotlin.reflect.jvm.jvmName


open class KafkaConsumerService(
    private val groupId: String,
    topic: String,
    private val callback: (ConsumerRecord<String, String>) -> Unit
) : Closeable {

    private var consumer: KafkaConsumer<String, String> = KafkaConsumer<String, String>(consumerProperties())

    init {
        consumer.subscribe(listOf(topic))
    }

    fun run() {
        while (true) {
            val records = consumer.poll(Duration.ofMillis(100))

            if (records.isEmpty) {
                continue
            }

            records.forEach {
                callback.invoke(it)
            }
        }
    }

    private fun consumerProperties(): Properties = hashMapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.jvmName,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.jvmName,
        ConsumerConfig.GROUP_ID_CONFIG to groupId,
        ConsumerConfig.CLIENT_ID_CONFIG to "$groupId-${UUID.randomUUID()}"
    ).toProperties()

    override fun close() {
        consumer.close()
    }
}