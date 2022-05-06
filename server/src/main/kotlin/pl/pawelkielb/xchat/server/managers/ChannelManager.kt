package pl.pawelkielb.xchat.server.managers

import com.mongodb.client.model.Filters
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.data.Channel
import pl.pawelkielb.xchat.data.Name
import pl.pawelkielb.xchat.server.create
import pl.pawelkielb.xchat.server.defaultPageSize
import pl.pawelkielb.xchat.server.list
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


private const val channels = "channels"

@Singleton
class ChannelManager @Inject constructor(private val db: CoroutineDatabase) {
    suspend fun list(
        createdAfter: Instant? = null,
        page: Int = 0,
        pageSize: Int = defaultPageSize
    ): List<Channel> {
        val filters = mutableSetOf<Bson>()
        if (createdAfter != null) {
            filters.add(Filters.gt(Channel::creationTimestamp.name, createdAfter.toEpochMilli()))
        }

        return db.list(channels, filters, page, pageSize, ascendingSortBy = Channel::creationTimestamp)
    }

    suspend fun create(name: Name, members: Set<Name>): Channel {
        val channel = Channel(name = name, members = members)
        db.create(channels, channel)
        return channel
    }
}
