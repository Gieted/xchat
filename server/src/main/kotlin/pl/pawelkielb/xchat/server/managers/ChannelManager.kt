package pl.pawelkielb.xchat.server.managers

import com.mongodb.client.model.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.data.Name
import pl.pawelkielb.xchat.server.Channel
import pl.pawelkielb.xchat.server.defaultPageSize
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


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

        return withContext(Dispatchers.IO) {
            db
                .getCollection<Channel>("channels")
                .find(*filters.toTypedArray())
                .skip(page * pageSize)
                .limit(pageSize)
                .toList()
        }
    }

    suspend fun create(name: Name, members: Set<Name>): Channel {
        val channel = Channel(name = name, members = members)

        withContext(Dispatchers.IO) {
            db.getCollection<Channel>("channels").insertOne(channel)
        }

        return channel
    }
}
