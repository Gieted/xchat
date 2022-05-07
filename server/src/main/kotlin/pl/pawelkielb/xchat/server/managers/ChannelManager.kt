package pl.pawelkielb.xchat.server.managers

import com.mongodb.client.model.Filters
import org.bson.Document
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.data.Name
import pl.pawelkielb.xchat.server.*
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


private const val channels = "channels"
private val creationTimestamp = Channel::creationTimestamp.name

@Singleton
class ChannelManager @Inject constructor(private val db: CoroutineDatabase) {
    suspend fun list(
        createdAfter: Instant? = null,
        page: Int = 0,
        pageSize: Int = defaultPageSize
    ): List<Channel> {
        val filters = mutableSetOf<Bson>()
        if (createdAfter != null) {
            filters.add(Filters.gt(creationTimestamp, createdAfter.toEpochMilli()))
        }

        return db.list(
            channels,
            filters,
            page,
            pageSize,
            sortCriteria = Document(creationTimestamp, SortDirection.ascending)
        )
    }

    suspend fun create(name: Name, members: Set<Name>): Channel {
        val channel = Channel(name = name, members = members)
        db.create(channels, channel)
        return channel
    }
}
