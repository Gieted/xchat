package pl.pawelkielb.xchat.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import pl.pawelkielb.xchat.data.Channel
import pl.pawelkielb.xchat.data.Name
import java.time.Instant

class XChatApi(private val httpClient: HttpClient, private val host: String) {
    fun listChannels(members: Set<Name>?, createdAfter: Instant?): List<Channel> = runBlocking {
        httpClient.get("$host/v1/channels") {
            if (members != null) {
                parameter("members", members.joinToString(","))
            }
            if (createdAfter != null) {
                parameter("createdAfter", createdAfter.toEpochMilli())
            }
        }.body()
    }
}
