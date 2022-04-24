package pl.pawelkielb.xchat

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import org.bson.types.ObjectId
import org.litote.kmongo.id.toId
import pl.pawelkielb.xchat.dagger.ApplicationContext
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
class ChannelsResource @Inject constructor(private val applicationContext: ApplicationContext) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getHello(): String = runBlocking {
        with(applicationContext) {
            json.encodeToString(Channel(ObjectId("6263e5fad135cc0d33f7de37").toId()).data())
        }
    }
}
