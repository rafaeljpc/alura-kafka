package io.github.rafaeljpc.alura.kafka.ecommerce.service.http.ecommerce

import io.github.rafaeljpc.alura.kafka.ecommerce.common.KafkaDispatcher
import io.github.rafaeljpc.alura.kafka.ecommerce.model.Order
import mu.KotlinLogging
import org.eclipse.jetty.http.HttpStatus
import java.math.BigDecimal
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = KotlinLogging.logger { }

class NewOrderServlet : HttpServlet() {

    private val orderDispatcher = KafkaDispatcher<Order>()
    private val emailDispatcher = KafkaDispatcher<String>()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val order = Order(
                email = req.parameterMap["email"]!!.first(),
                amount = BigDecimal(req.parameterMap["amount"]!!.first()),
                orderId = UUID.randomUUID().toString(),
            )

            orderDispatcher.send("ECOMMERCE_NEW_ORDER", order.email, order)
            emailDispatcher.send(
                "ECOMMERCE_SEND_EMAIL", order.email,
                """
                Thank you for your order! We are processing your order
                Order Number ${order.orderId}
                """.trimIndent()
            )

            logger.info { "New Order sent" }
            resp.writer.println("New Order sent")
            resp.status = HttpStatus.OK_200
        } catch (e: Exception) {
            logger.error(e) { e.message }
            throw ServletException(e)
        }
    }
}
