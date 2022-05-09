package pl.pawelkielb.xchat.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.pawelkielb.xchat.TransferSettings.fileChunkSizeInBytes
import pl.pawelkielb.xchat.data.*
import java.io.InputStream
import java.io.OutputStream
import java.time.Instant
import java.util.*
import java.util.function.Consumer

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

    suspend fun listMessages(channel: UUID, sentBefore: Instant?, page: Int, pageSize: Int): List<Message> =
        httpClient.get("$host/v1/channels/$channel/messages") {
            accept(ContentType.Application.Json)
            authorization(user.value())
            if (sentBefore != null) {
                parameter("sentBefore", sentBefore.toEpochMilli())
            }
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()

    suspend fun sendMessage(channel: UUID, message: SendMessageRequest): Message =
        httpClient.post("$host/v1/channels/$channel/messages") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            authorization(user.value())
            setBody(message)
        }.body()

    suspend fun uploadFile(
        channel: UUID,
        file: InputStream,
        name: Name,
        size: Long,
        progressConsumer: Consumer<Double>,
    ) = httpClient.post("$host/v1/channels/$channel/files") {
        accept(ContentType.Application.Json)
        authorization(user.value())
        setBody(MultiPartFormDataContent(
            formData {
                appendInput(key = "file", block = { file.asInput() }, size = size, headers = Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=$name")
                })
            }
        ))
        onUpload { bytesSentTotal, contentLength ->
            progressConsumer.accept(bytesSentTotal.toDouble() / contentLength)
        }
    }.also { file.close() }

    suspend fun downloadFile(
        channel: UUID,
        file: Name,
        outputStream: OutputStream,
        progressConsumer: Consumer<Double>
    ) = try {
        httpClient.prepareGet("$host/v1/channels/$channel/files/$file") {
            accept(ContentType.Application.Any)
            authorization(user.value())
        }.execute { response ->
            val body: ByteReadChannel = response.body()
            val totalSize = response.contentLength() ?: 0
            var readBytes = 0
            while (!body.isClosedForRead) {
                val packet = body.readRemaining(fileChunkSizeInBytes.toLong())
                while (!packet.isEmpty) {
                    val bytes = packet.readBytes()
                    withContext(Dispatchers.IO) {
                        outputStream.write(bytes)
                    }
                    readBytes += bytes.size
                    progressConsumer.accept(readBytes.toDouble() / totalSize)
                }
            }
            withContext(Dispatchers.IO) {
                outputStream.close()
            }
        }
    } catch (e: ClientRequestException) {
        if (e.response.status == HttpStatusCode.NotFound) {
            throw NoSuchElementException(file.value())
        } else throw e
    }
}

private fun HttpRequestBuilder.authorization(value: String) {
    headers {
        append(HttpHeaders.Authorization, value)
    }
}
