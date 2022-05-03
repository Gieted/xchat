package pl.pawelkielb.xchat

import pl.pawelkielb.xchat.services.Server
import java.lang.System.getenv
import kotlin.system.exitProcess


fun main() {
    val port = runCatching { getenv("PORT")?.toInt() }.getOrElse {
        System.err.println("There was an error while parsing PORT")
        exitProcess(1)
    }
    
    val server = if (port != null) Server(port) else Server()
    server.start()
}
