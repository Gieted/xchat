package pl.pawelkielb.xchat.server.managers

import com.mongodb.client.model.Filters
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.data.Channel
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
        members: Set<Name>,
        createdAfter: Instant? = null,
        page: Int = 0,
        pageSize: Int = defaultPageSize,
        accessingUser: Name
    ): List<Channel> {
        val filters = mutableSetOf<Bson>()
        filters.add(Filters.all(Channel::members.name, members.map { it.value().lowercase() }))
        if (createdAfter != null) {
            filters.add(Filters.gt(creationTimestamp, createdAfter.toEpochMilli()))
        }

        return db.list<ChannelMongoEntry>(
            channels,
            filters,
            page,
            pageSize,
            ascendingSortBy = Channel::creationTimestamp
        ).map { it.toChannel(accessingUser) }
    }

    suspend fun create(name: Name?, members: Set<Name>, accessingUser: Name): Channel {
        val channel = ChannelMongoEntry(name = name, members = members)
        db.create(channels, channel)

        return channel.toChannel(accessingUser)
    }
}
