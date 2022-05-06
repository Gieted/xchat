package pl.pawelkielb.xchat.server.managers

import com.mongodb.client.model.Filters
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.data.Message
import pl.pawelkielb.xchat.data.MessageMongoEntry
import pl.pawelkielb.xchat.server.create
import pl.pawelkielb.xchat.server.defaultPageSize
import pl.pawelkielb.xchat.server.list
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


private const val messages = "messages"

@Singleton
class MessageManager @Inject constructor(private val db: CoroutineDatabase) {

    suspend fun list(channel: UUID, page: Int = 0, pageSize: Int = defaultPageSize): List<Message> {
        val filters = setOf(Filters.eq(MessageMongoEntry::channel.name, channel.toString()))

        val entries = db.list<MessageMongoEntry>(
            messages,
            filters,
            page,
            pageSize,
            descendingSortBy = MessageMongoEntry::sendingTimestamp
        )
        return entries.map { it.message }
    }

    suspend fun sendMessage(channel: UUID, message: Message) {
        val mongoEntry = MessageMongoEntry(message, channel)
        db.create(messages, mongoEntry)
    }
}
