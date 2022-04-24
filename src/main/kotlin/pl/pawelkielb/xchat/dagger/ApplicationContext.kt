package pl.pawelkielb.xchat.dagger

import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.services.ChannelResource
import pl.pawelkielb.xchat.services.ChannelsResource
import pl.pawelkielb.xchat.services.RootServlet
import pl.pawelkielb.xchat.services.V1Resource
import pl.pawelkielb.xchat.ChannelData
import javax.inject.Inject
import javax.inject.Singleton
import pl.pawelkielb.xchat.model.Channel
import pl.pawelkielb.xchat.services.Server
import javax.inject.Provider

@Singleton
data class ApplicationContext @Inject constructor(
    val rootServlet: Provider<RootServlet>,
    val db: Provider<CoroutineDatabase>,
    val v1Resource: Provider<V1Resource>,
    val channelsResource: Provider<ChannelsResource>,
    val channelResource: Provider<ChannelResource>
) {
    fun Channel(id: Id<ChannelData>) = Channel(id, db.get())

    fun Server(port: Int = Server.defaultPort) =
        Server(port, rootServlet.get(), v1Resource.get(), channelsResource.get(), channelResource.get())
}
