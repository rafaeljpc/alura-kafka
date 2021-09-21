package io.github.rafaeljpc.alura.kafka.ecommerce.service.new.order

import io.github.rafaeljpc.alura.kafka.ecommerce.common.KafkaDispatcher
import io.github.rafaeljpc.alura.kafka.ecommerce.model.Order
import java.math.BigDecimal
import java.util.*

private const val QTD = 10

fun main() {
    val orderDispatcher = KafkaDispatcher<Order>()
    val emailDispatcher = KafkaDispatcher<String>()

    repeat(QTD) {
        val order = Order(
            email = "user${(1..100).random()}@email.com",
            orderId = UUID.randomUUID().toString(),
            amount = BigDecimal((1..5000).random())
        )

        orderDispatcher.send("ECOMMERCE_NEW_ORDER", order.email, order)
        emailDispatcher.send(
            "ECOMMERCE_SEND_EMAIL", order.email,
            """
            Thank you for your order! We are processing your order
            Order Number ${order.orderId}
            """.trimIndent()
        )
    }


}