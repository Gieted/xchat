package pl.pawelkielb.xchat.server.dagger

import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.server.Server
import pl.pawelkielb.xchat.server.routes.ChannelsResource
import pl.pawelkielb.xchat.server.routes.RootServlet
import pl.pawelkielb.xchat.server.routes.V1Resource
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
data class ApplicationContext @Inject constructor(
    val rootServlet: Provider<RootServlet>,
    val db: Provider<CoroutineDatabase>,
    val v1Resource: Provider<V1Resource>,
    val channelsResource: Provider<ChannelsResource>,
) {
    fun Server(port: Int = Server.defaultPort) =
        Server(
            port,
            rootServlet.get(),
            v1Resource.get(),
            channelsResource.get(),
        )
}
