package pl.pawelkielb.xchat.server

import java.io.File
import java.lang.System.getenv
import kotlin.system.exitProcess


fun main() {
    val port = runCatching { getenv("PORT")?.toInt() }.getOrElse {
        System.err.println("There was an error while parsing PORT")
        exitProcess(1)
    } ?: Server.defaultPort

    val server = Server(port, File("."))
    server.start()
}
