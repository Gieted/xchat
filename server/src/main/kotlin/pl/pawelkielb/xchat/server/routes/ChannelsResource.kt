package pl.pawelkielb.xchat.server.routes

import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.pawelkielb.xchat.data.CreateChannelRequest
import pl.pawelkielb.xchat.data.Name
import pl.pawelkielb.xchat.defaultPageSize
import pl.pawelkielb.xchat.server.*
import pl.pawelkielb.xchat.server.managers.ChannelManager
import javax.inject.Inject
import javax.inject.Singleton

private const val createdAfter = "createdAfter"

@Singleton
@Path("/channels")
class ChannelsResource @Inject constructor(private val channelManager: ChannelManager) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun list(
        @Context headers: HttpHeaders,
        @QueryParam("members") membersString: String?,
        @QueryParam(createdAfter) createdAfterString: String?,
        @QueryParam("page") pageString: String?,
        @QueryParam("pageSize") pageSizeString: String?
    ): String = runBlocking(Dispatchers.Default) {
        val user = parseUser(headers)

        val members = membersString?.split(",")?.map { Name.of(it) }?.toSet() ?: emptySet()
        val createdAfter = parseInstant(createdAfterString, createdAfter)

        val page = parsePage(pageString) ?: 0
        val pageSize = parsePageSize(pageSizeString) ?: defaultPageSize

        val channels = channelManager.list(members, createdAfter, page, pageSize, user)
        Json.encodeToString(channels)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun create(@Context headers: HttpHeaders, createChannelRequestJson: String) = runBlocking(Dispatchers.Default) {
        val user = parseUser(headers)

        val createChannelRequest = runCatching {
            Json.decodeFromString<CreateChannelRequest>(createChannelRequestJson)
        }.getOrElse { throw badRequest(it.message) }

        val createdChannel =
            channelManager.create(createChannelRequest.name, createChannelRequest.members, user)
        Json.encodeToString(createdChannel)
    }
}
