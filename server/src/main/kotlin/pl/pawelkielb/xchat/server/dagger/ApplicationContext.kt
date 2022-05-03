package pl.pawelkielb.xchat.server.dagger

import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.server.services.ChannelResource
import pl.pawelkielb.xchat.server.services.ChannelsResource
import pl.pawelkielb.xchat.server.services.RootServlet
import pl.pawelkielb.xchat.server.services.V1Resource
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
data class ApplicationContext @Inject constructor(
    val rootServlet: Provider<RootServlet>,
    val db: Provider<CoroutineDatabase>,
    val v1Resource: Provider<V1Resource>,
    val channelsResource: Provider<ChannelsResource>,
    val channelResource: Provider<ChannelResource>
) {
    fun Server(port: Int = pl.pawelkielb.xchat.server.services.Server.defaultPort) =
        pl.pawelkielb.xchat.server.services.Server(
            port,
            rootServlet.get(),
            v1Resource.get(),
            channelsResource.get(),
            channelResource.get()
        )
}
