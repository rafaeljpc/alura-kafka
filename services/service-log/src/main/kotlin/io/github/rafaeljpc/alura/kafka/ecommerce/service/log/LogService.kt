package io.github.rafaeljpc.alura.kafka.ecommerce.service.log

import io.github.rafaeljpc.alura.kafka.ecommerce.common.KafkaService
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.common.serialization.StringDeserializer
import java.util.regex.Pattern

private val logger = KotlinLogging.logger { }

fun main() {
    KafkaService(
        groupId = "io.github.rafaeljpc.alura.kafka.ecommerce.service.log",
        topicPattern = Pattern.compile("ECOMMERCE.*"),
        type = String::class,
        properties = mapOf(
            VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name
        ),
        parse = { record ->
            logger.info {
                """
                ---------------------------------------------
                LOG: ${record.topic()}
                key=${record.key()}
                value=${record.value()}
                partition=${record.partition()}
                offset=${record.offset()}
            """.trimIndent()
            }
        }
    ).run()
}