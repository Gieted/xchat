package pl.pawelkielb.xchat.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import pl.pawelkielb.xchat.data.Channel
import pl.pawelkielb.xchat.data.CreateChannelRequest
import pl.pawelkielb.xchat.data.Name
import java.time.Instant

class XChatApi(private val httpClient: HttpClient, private val host: String, private val user: Name) {
    suspend fun listChannels(members: Set<Name>?, createdAfter: Instant?): List<Channel> = coroutineScope {
        httpClient.get("$host/v1/channels") {
            accept(ContentType.Application.Json)
            authorization(user.value())
            if (members != null) {
                parameter("members", members.joinToString(","))
            }
            if (createdAfter != null) {
                parameter("createdAfter", createdAfter.toEpochMilli())
            }
        }.body()
    }

    suspend fun createChannel(name: Name?, members: Set<Name>) {
        httpClient.post("$host/v1/channels") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            authorization(user.value())
            setBody(CreateChannelRequest(name, members))
        }
    }
}

private fun HttpRequestBuilder.authorization(value: String) {
    headers {
        append(HttpHeaders.Authorization, value)
    }
}
