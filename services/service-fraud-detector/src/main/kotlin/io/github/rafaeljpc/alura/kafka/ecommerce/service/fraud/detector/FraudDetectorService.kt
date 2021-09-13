package io.github.rafaeljpc.alura.kafka.ecommerce.service.fraud.detector

import io.github.rafaeljpc.alura.kafka.ecommerce.common.KafkaService
import io.github.rafaeljpc.alura.kafka.ecommerce.model.Order
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

fun main() {
    KafkaService(
        groupId = "io.github.rafaeljpc.alura.kafka.ecommerce.service.fraud.detector",
        topic = "ECOMMERCE_NEW_ORDER",
        type = Order::class,
        parse = { record ->
            logger.info {
                """
                ------------------------------------------
                Processing new order, checking for fraud
                k=${record.key()}
                v=${record.value()}
                part=${record.partition()}
                offset=${record.offset()}
            """.trimIndent()
            }

            Thread.sleep(5000)

            logger.info { "Order processed" }
        }
    ).run()
}