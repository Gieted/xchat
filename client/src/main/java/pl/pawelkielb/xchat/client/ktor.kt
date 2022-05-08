@file:JvmName("Ktor")

package pl.pawelkielb.xchat.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import pl.pawelkielb.xchat.Logger
import io.ktor.client.plugins.logging.Logger as KtorLogger


fun createClient(logger: Logger) = HttpClient(CIO) {
    install(Logging) {
        this.logger = object : KtorLogger {
            override fun log(message: String) = logger.info(message)
        }
        level = LogLevel.HEADERS
    }
}
