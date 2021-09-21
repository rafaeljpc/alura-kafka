package io.github.rafaeljpc.alura.kafka.ecommerce.service.fraud.detector

import io.github.rafaeljpc.alura.kafka.ecommerce.common.KafkaDispatcher
import io.github.rafaeljpc.alura.kafka.ecommerce.common.KafkaService
import io.github.rafaeljpc.alura.kafka.ecommerce.model.Order
import mu.KotlinLogging
import java.math.BigDecimal

private val logger = KotlinLogging.logger { }

private val orderDispatcher = KafkaDispatcher<Order>()

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

            Thread.sleep(2000)

            val order = record.value()

            if (isFraud(order)) {
                logger.info { "Fraud detected order=$order" }
                orderDispatcher.send("ECOMMERCE_ORDER_REJECTED", record.key(), order)
            } else {
                logger.info { "Order processed" }
                orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", record.key(), order)
            }
        }
    ).run()
}

private fun isFraud(order: Order) = BigDecimal(4500) <= order.amount