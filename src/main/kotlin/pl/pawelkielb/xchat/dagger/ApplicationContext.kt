package pl.pawelkielb.xchat.dagger

import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.ChannelsResource
import pl.pawelkielb.xchat.RootServlet
import pl.pawelkielb.xchat.V1Resource
import pl.pawelkielb.xchat.data.ChannelData
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
    val channelsResource: Provider<ChannelsResource>
) {
    fun Channel(id: Id<ChannelData>) = Channel(id, db.get())

    fun Server(port: Int = Server.defaultPort) =
        Server(port, rootServlet.get(), v1Resource.get(), channelsResource.get())
}
