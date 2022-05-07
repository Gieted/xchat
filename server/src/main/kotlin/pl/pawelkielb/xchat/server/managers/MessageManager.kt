package pl.pawelkielb.xchat.server.managers

import com.mongodb.client.model.Filters
import org.bson.Document
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.data.Message
import pl.pawelkielb.xchat.server.*
import java.time.Instant
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


private const val messages = "messages"
private val message = MessageMongoEntry::message.name
private val sendingTimestamp = Message::sendingTimestamp.name

@Singleton
class MessageManager @Inject constructor(private val db: CoroutineDatabase) {

    suspend fun list(
        channel: UUID,
        sentBefore: Instant?,
        page: Int = 0,
        pageSize: Int = defaultPageSize
    ): List<Message> {
        val filters = mutableSetOf<Bson>()
        filters.add(Filters.eq(MessageMongoEntry::channel.name, channel.toString()))
        if (sentBefore != null) {
            filters.add(Filters.lt("$message.$sendingTimestamp", sentBefore.toEpochMilli()))
        }

        val entries = db.list<MessageMongoEntry>(
            messages,
            filters,
            page,
            pageSize,
            sortCriteria = Document("$message.$sendingTimestamp", SortDirection.descending)
        )
        return entries.map { it.message }
    }

    suspend fun sendMessage(channel: UUID, message: Message) {
        val mongoEntry = MessageMongoEntry(message, channel)
        db.create(messages, mongoEntry)
    }
}
