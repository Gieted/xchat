package pl.pawelkielb.xchat.services

import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import org.litote.kmongo.coroutine.CoroutineDatabase
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
class ChannelsResource @Inject constructor() {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun list(@Context request: HttpServletRequest) {

    }
}

@Singleton
@Path("/channels/{id}")
class ChannelResource @Inject constructor(private val db: CoroutineDatabase) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun get(
        @PathParam("id") channelId: String,
        @Context request: HttpServletRequest
    ) {

    }
}
