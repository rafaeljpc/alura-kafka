package io.github.rafaeljpc.alura.kafka.ecommerce.service.http.ecommerce

import ch.qos.logback.access.jetty.RequestLogImpl

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

fun main() {
    val server = Server(8088)

    server.requestLog = RequestLogImpl()

    val contextHandler = ServletContextHandler()
    contextHandler.contextPath = "/"
    contextHandler.addServlet(ServletHolder(NewOrderServlet()), "/new")
    server.handler = contextHandler

    server.start()
    server.join()
}