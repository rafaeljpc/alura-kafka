package io.github.rafaeljpc.alura.kafka.ecommerce.service.reading.report

import io.github.rafaeljpc.alura.kafka.ecommerce.common.KafkaService
import io.github.rafaeljpc.alura.kafka.ecommerce.model.User
import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger { }

private val REPORT_TEMPLATE = File({}.javaClass.getResource("report-template.txt")!!.toURI())

fun main() {
    KafkaService(
        groupId = "io.github.rafaeljpc.alura.kafka.ecommerce.service.reading.report",
        topic = "USER_GENERATE_READING_REPORT",
        type = User::class,
        parse = { record ->
            logger.info {
                """
                ------------------------------------------
                Processing report for ${record.value()}
            """.trimIndent()
            }

            val user = record.value()
            val target = File("build/${user.id}-report.txt")

            target.mkdirs()
            REPORT_TEMPLATE.copyTo(target, true)
            target.appendText(
                """
                
                Created for ${user.id}
            """.trimIndent()
            )

            logger.info { "Report Created: ${target.absolutePath}" }
        }
    ).run()
}
