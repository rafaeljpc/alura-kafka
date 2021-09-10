package io.github.rafaeljpc.alura.kafka.ecommerce.model

import java.math.BigDecimal

data class Order(
    val userId: String,
    val orderId: String,
    val amount: BigDecimal
)
