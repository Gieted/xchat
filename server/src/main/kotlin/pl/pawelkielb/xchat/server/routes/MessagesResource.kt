package pl.pawelkielb.xchat.server.routes

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.pawelkielb.xchat.data.Message
import pl.pawelkielb.xchat.defaultPageSize
import pl.pawelkielb.xchat.server.*
import pl.pawelkielb.xchat.server.managers.MessageManager
import javax.inject.Inject
import javax.inject.Singleton


private const val sentBefore = "sentBefore"

@Singleton
@Path("/channels/{channel}/messages")
class MessagesResource @Inject constructor(private val messageManager: MessageManager) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun list(
        @PathParam("channel") channelString: String,
        @QueryParam(sentBefore) sentBeforeString: String?,
        @QueryParam("page") pageString: String?,
        @QueryParam("pageSize") pageSizeString: String?,
    ): String = runBlocking(Dispatchers.Default) {
        val channel = parseChannel(channelString)

        val sentBefore = parseInstant(sentBeforeString, sentBefore)
        val page = parsePage(pageString) ?: 0
        val pageSize = parsePageSize(pageSizeString) ?: defaultPageSize

        val messages = messageManager.list(channel, sentBefore, page, pageSize)

        Json.encodeToString(messages)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun send(
        @PathParam("channel") channelString: String,
        messageJson: String
    ) = runBlocking(Dispatchers.Default) {
        val channel = parseChannel(channelString)

        val message = runCatching { Json.decodeFromString<Message>(messageJson) }.getOrElse {
            throw badRequest(it.message)
        }

        messageManager.sendMessage(channel, message)
        Json.encodeToString(message)
    }
}
