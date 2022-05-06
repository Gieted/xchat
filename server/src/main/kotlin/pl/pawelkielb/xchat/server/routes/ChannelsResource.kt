package pl.pawelkielb.xchat.server.routes

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.pawelkielb.xchat.data.CreateChannelRequest
import pl.pawelkielb.xchat.server.badRequest
import pl.pawelkielb.xchat.server.cannotParseException
import pl.pawelkielb.xchat.server.managers.ChannelManager
import pl.pawelkielb.xchat.server.parsePage
import pl.pawelkielb.xchat.server.parsePageSize
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

private const val createdAfter = "createdAfter"

@Singleton
@Path("/channels")
class ChannelsResource @Inject constructor(private val channelManager: ChannelManager) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun list(
        @QueryParam(createdAfter) createdAfterString: String?,
        @QueryParam("page") pageString: String?,
        @QueryParam("pageSize") pageSizeString: String?
    ): String = runBlocking {
        withContext(Dispatchers.Default) {
            val createdAfter = if (createdAfterString != null)
                runCatching { Instant.ofEpochMilli(createdAfterString.toLong()) }
                    .getOrElse { throw cannotParseException(createdAfter, createdAfterString) }
            else null

            val page = parsePage(pageString)
            val pageSize = parsePageSize(pageSizeString)

            val channels = when {
                page != null && pageSize != null -> channelManager.list(createdAfter, page, pageSize)
                page != null && pageSize == null -> channelManager.list(createdAfter, page)
                page == null && pageSize != null -> channelManager.list(createdAfter, pageSize = pageSize)
                else -> channelManager.list(createdAfter)
            }
            Json.encodeToString(channels)
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun create(createChannelRequestJson: String) = runBlocking {
        withContext(Dispatchers.Default) {
            val createChannelRequest = runCatching {
                Json.decodeFromString<CreateChannelRequest>(createChannelRequestJson)
            }.getOrElse { throw badRequest(it.message) }

            val createdChannel = channelManager.create(createChannelRequest.name, createChannelRequest.members)
            Json.encodeToString(createdChannel)
        }
    }
}
