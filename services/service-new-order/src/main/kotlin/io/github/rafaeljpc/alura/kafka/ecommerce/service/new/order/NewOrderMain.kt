package io.github.rafaeljpc.alura.kafka.ecommerce.service.new.order

import io.github.rafaeljpc.alura.kafka.ecommerce.common.KafkaDispatcher
import io.github.rafaeljpc.alura.kafka.ecommerce.model.Order
import java.math.BigDecimal
import java.util.*

fun main() {
    val orderDispatcher = KafkaDispatcher<Order>()
    val emailDispatcher = KafkaDispatcher<String>()

    val order = Order(
        userId = UUID.randomUUID().toString(),
        orderId = UUID.randomUUID().toString(),
        amount = BigDecimal((1..5000).random())
    )

    orderDispatcher.send("ECOMMERCE_NEW_ORDER", order.userId, order)
    emailDispatcher.send(
        "ECOMMERCE_SEND_EMAIL", order.userId, """
        Thank you for your order! We are processing your order
        Order Number ${order.orderId}
    """.trimIndent()
    )
}