package io.github.rafaeljpc.alura.kafka.ecommerce.service.user

import java.sql.Connection
import java.sql.DriverManager

object UserRepository {
    private const val URL = "jdbc:sqlite:build/users_database.db"
    private val connection: Connection = DriverManager.getConnection(URL)

    init {
        connection.createStatement().execute(
            """
            create table IF NOT EXISTS Users (
                UUID varchar(200) primary key,
                email varchar(200)
            )
        """.trimIndent()
        )
    }

    fun exists(email: String): Boolean {
        val ps = connection.prepareStatement("select UUID from Users where email=? limit 1")
        ps.setString(1, email)
        val rs = ps.executeQuery()
        return rs.next()
    }

    fun allUsers(): List<String> {
        val rs = connection.prepareStatement("select * from Users").executeQuery()
        val result = mutableListOf<String>()
        while (rs.next()) {
            result.add(rs.getString("UUID") + rs.getString("email"))
        }
        return result
    }

    fun insertUser(userId: String, email: String) {
        val ps = connection.prepareStatement("insert into Users (UUID, email) values (?, ?)")
        ps.setString(1, userId)
        ps.setString(2, email)
        ps.execute()
    }
}