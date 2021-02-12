package io.github.rafaeljpc.alura.kafka.ecommerce.consumers

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord

private val log = KotlinLogging.logger {}

private val process: (ConsumerRecord<String, String>) -> Unit = {
    log.info(
        """
            ------------------------------------------
            Processing New Order
            key = ${it.key()}
            value = ${it.value()}
            partition = ${it.partition()}
            offset = ${it.offset()}
        """.trimIndent()
    )

    Thread.sleep(1000)
    log.info(
        """
                Order processed
                ------------------------------------------
                """.trimIndent()
    )
}

fun main() {
    KafkaConsumerService(
        groupId = "FraudDetectorService",
        topic = "ECOMMERCE_NEW_ORDER",
        callback = process
    ).use { service -> service.run() }
}