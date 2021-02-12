package io.github.rafaeljpc.alura.kafka.ecommerce.consumers

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord

private val log = KotlinLogging.logger {}

private val process: (ConsumerRecord<String, String>) -> Unit = {
    log.info(
        """
            ------------------------------------------
            Sending email
            key = ${it.key()}
            value = ${it.value()}
            partition = ${it.partition()}
            offset = ${it.offset()}
        """.trimIndent()
    )

    Thread.sleep(500)
    log.info(
        """
                email sent
                ------------------------------------------
                """.trimIndent()
    )
}

fun main() {
    KafkaConsumerService(
        groupId = "EmailService",
        topic = "ECOMMERCE_SEND_EMAIL",
        callback = process
    ).use { service -> service.run() }
}

