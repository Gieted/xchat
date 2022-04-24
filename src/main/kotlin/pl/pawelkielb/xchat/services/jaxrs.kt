package pl.pawelkielb.xchat.services

import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.ForbiddenException
import jakarta.ws.rs.GET
import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.id.toId
import pl.pawelkielb.xchat.access_control.ResourceAccessDeniedException
import pl.pawelkielb.xchat.dagger.ApplicationContext
import pl.pawelkielb.xchat.ChannelData
import pl.pawelkielb.xchat.json
import pl.pawelkielb.xchat.utils.listResource
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
class ChannelsResource @Inject constructor(private val db: CoroutineDatabase) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun list(@Context request: HttpServletRequest) = handleRequest(request) { authorization ->
        db.listResource<ChannelData>(authorization)
    }
}

@Singleton
@Path("/channels/{id}")
class ChannelResource @Inject constructor(private val applicationContext: ApplicationContext) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun get(@PathParam("id") channelId: String): String = runBlocking {
        with(applicationContext) {
            val channel = Channel(ObjectId(channelId).toId())

            json.encodeToString(channel.data())
        }
    }
}

private inline fun <reified T> handleRequest(
    request: HttpServletRequest,
    noinline fn: suspend (authorization: String?) -> T
) = runBlocking {
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION)

        try {
            json.encodeToString(fn(authorization))
        } catch (e: ResourceAccessDeniedException) {
            throw if (authorization == null) NotAuthorizedException(e) else ForbiddenException(e)
        }
    }
