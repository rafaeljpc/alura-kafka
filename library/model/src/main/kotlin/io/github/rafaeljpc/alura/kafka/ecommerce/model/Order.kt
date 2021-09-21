package io.github.rafaeljpc.alura.kafka.ecommerce.model

import java.math.BigDecimal

data class Order(
    val orderId: String,
    val email: String,
    val amount: BigDecimal,
)
