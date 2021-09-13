package io.github.rafaeljpc.alura.kafka.ecommerce.service.email

import io.github.rafaeljpc.alura.kafka.ecommerce.common.KafkaService
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

private val logger = KotlinLogging.logger { }

fun main() {
    KafkaService(
        groupId = "io.github.rafaeljpc.alura.kafka.ecommerce.service.email",
        topic = "ECOMMERCE_SEND_EMAIL",
        type = String::class,
        properties = mapOf(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name
        ),
        parse = { record ->
            logger.info {
                """
                ---------------------------------------------------------------
                Send email
                ${record.key()}
                ${record.value()}
                ${record.partition()}
                ${record.offset()}                
            """.trimIndent()
            }

            Thread.sleep(1000)

            logger.info { "Email sent" }
        }
    ).run()
}