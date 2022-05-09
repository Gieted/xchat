package pl.pawelkielb.xchat.server.routes

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@Path("/")
class V1Resource @Inject constructor() {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun get() = "xchat API v1"
}
