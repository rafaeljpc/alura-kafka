package io.github.rafaeljpc.alura.kafka.ecommerce.service.user

import io.github.rafaeljpc.alura.kafka.ecommerce.common.KafkaService
import io.github.rafaeljpc.alura.kafka.ecommerce.model.Order
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger { }

fun main() {
    val count = UserRepository.allUsers().map {
        logger.info { "User $it" }
    }.count()
    logger.info { "Users $count found" }

    KafkaService(
        groupId = "io.github.rafaeljpc.alura.kafka.ecommerce.service.user.create",
        topic = "ECOMMERCE_NEW_ORDER",
        type = Order::class,
        parse = { record ->
            logger.info {
                """
                ------------------------------------------
                Processing new order, checking for new user
                v=${record.value()}
            """.trimIndent()
            }

            if (!UserRepository.exists(record.value().email)) {
                UserRepository.insertUser(UUID.randomUUID().toString(), record.value().email)
                logger.info { "New user created ${UserRepository.findByEmail(record.value().email)}" }
            }
        }
    ).run()
}