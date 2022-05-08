@file:JvmName("Ktor")

package pl.pawelkielb.xchat.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import pl.pawelkielb.xchat.client.logger.Logger
import io.ktor.client.plugins.logging.Logger as KtorLogger


fun createClient(logger: Logger) = HttpClient(CIO) {
    expectSuccess = true
    install(Logging) {
        this.logger = object : KtorLogger {
            override fun log(message: String) = logger.info(message)
        }
        level = LogLevel.BODY
    }

    install(ContentNegotiation) {
        json()
    }
}
