package pl.pawelkielb.xchat.server

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@Path("/")
class V1Resource @Inject constructor() {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun get() = "xchat API v1"
}

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

private fun badRequest(message: String?) =
    WebApplicationException(
        Response
            .status(Response.Status.BAD_REQUEST)
            .entity(message ?: "")
            .type(MediaType.TEXT_PLAIN)
            .build()
    )
