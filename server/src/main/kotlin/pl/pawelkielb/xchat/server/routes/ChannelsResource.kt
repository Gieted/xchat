package pl.pawelkielb.xchat.server.routes

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.pawelkielb.xchat.server.CreateChannelRequest
import pl.pawelkielb.xchat.server.managers.ChannelManager
import pl.pawelkielb.xchat.server.utils.badRequest
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Path("/channels")
class ChannelsResource @Inject constructor(private val channelManager: ChannelManager) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun list(@QueryParam("createdAfter") createdAfterString: String?): String = runBlocking {
        val createdAfter = if (createdAfterString != null)
            runCatching { Instant.ofEpochMilli(createdAfterString.toLong()) }
                .getOrElse { throw badRequest("Cannot parse 'createdAfter': $createdAfterString") }
        else null

        val channels = channelManager.list(createdAfter)
        Json.encodeToString(channels)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun create(createChannelRequestJson: String) = runBlocking {
        val createChannelRequest = runCatching {
            Json.decodeFromString<CreateChannelRequest>(createChannelRequestJson)
        }.getOrElse { throw badRequest(it.message) }

        val createdChannel = channelManager.create(createChannelRequest.name, createChannelRequest.members)
        Json.encodeToString(createdChannel)
    }
}
