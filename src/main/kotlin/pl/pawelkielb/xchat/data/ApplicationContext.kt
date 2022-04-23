package pl.pawelkielb.xchat.data

import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.services.V1ResourceServlet
import pl.pawelkielb.xchat.services.V1Servlet
import javax.inject.Inject
import javax.inject.Singleton
import pl.pawelkielb.xchat.model.Channel
import pl.pawelkielb.xchat.services.Server
import javax.inject.Provider

@Singleton
data class ApplicationContext @Inject constructor(
    val v1Servlet: Provider<V1Servlet>,
    val v1ResourceServlet: Provider<V1ResourceServlet>,
    val db: Provider<CoroutineDatabase>
) {
    fun Channel(id: Id<ChannelData>) = Channel(id, db.get())
    
    fun Server(port: Int = Server.defaultPort) = Server(port, v1Servlet.get(), v1ResourceServlet.get())
}
