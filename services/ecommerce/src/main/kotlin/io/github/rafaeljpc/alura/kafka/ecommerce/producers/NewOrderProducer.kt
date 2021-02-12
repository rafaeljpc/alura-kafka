package io.github.rafaeljpc.alura.kafka.ecommerce.producers

import mu.KotlinLogging
import java.util.*
import kotlin.random.Random

private val log = KotlinLogging.logger {}

fun main() {
    KafkaDispatcher().use { producer ->

        repeat(100) {

            val rnd = Random(System.currentTimeMillis())
            val key = UUID.randomUUID().toString()
            val value = "$key,${rnd.nextInt(1, 1000)},${rnd.nextInt(1, 20) * 100}"
            producer.send("ECOMMERCE_NEW_ORDER", key, value)

            val email = "Thank you for your order! We are processing your order"
            producer.send("ECOMMERCE_SEND_EMAIL", key, email)
        }
    }

}
