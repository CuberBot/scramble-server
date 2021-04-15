package net.lz1998

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.lz1998.plugins.*

fun main() {
    embeddedServer(Netty, port = 12014, host = "0.0.0.0") {
        configureMonitoring()
        configureRouting()
    }.start(wait = true)
}
