package pl.pawelkielb.xchat.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import pl.pawelkielb.xchat.data.Channel
import pl.pawelkielb.xchat.data.CreateChannelRequest
import pl.pawelkielb.xchat.data.Message
import pl.pawelkielb.xchat.data.Name
import java.time.Instant
import java.util.*

class XChatApi(private val httpClient: HttpClient, private val host: String, private val user: Name) {
    suspend fun listChannels(members: Set<Name>?, createdAfter: Instant?, page: Int, pageSize: Int): List<Channel> =
        httpClient.get("$host/v1/channels") {
            accept(ContentType.Application.Json)
            authorization(user.value())
            if (members != null) {
                parameter("members", members.joinToString(","))
            }
            if (createdAfter != null) {
                parameter("createdAfter", createdAfter.toEpochMilli())
            }
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()

    suspend fun createChannel(name: Name?, members: Set<Name>): Channel =
        httpClient.post("$host/v1/channels") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            authorization(user.value())
            setBody(CreateChannelRequest(name, members))
        }.body()

    suspend fun listMessages(channel: UUID, sentBefore: Instant?, page: Int, pageSize: Int): List<Message> {
        return httpClient.get("$host/v1/channels/$channel/messages") {
            accept(ContentType.Application.Json)
            authorization(user.value())
            if (sentBefore != null) {
                parameter("sentBefore", sentBefore.toEpochMilli())
            }
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()
    }
}

private fun HttpRequestBuilder.authorization(value: String) {
    headers {
        append(HttpHeaders.Authorization, value)
    }
}
