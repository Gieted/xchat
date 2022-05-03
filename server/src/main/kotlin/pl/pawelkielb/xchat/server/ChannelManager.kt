package pl.pawelkielb.xchat.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.data.Name
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
        val query = Document()
        if (createdAfter != null) {
            query.append("createdAfter", Document("\$gt", createdAfter.toEpochMilli()))
        }

        return withContext(Dispatchers.IO) {
            db
                .getCollection<Channel>("channels")
                .find(query)
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
