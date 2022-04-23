package pl.pawelkielb.xchat.model

import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.data.ChannelData
import pl.pawelkielb.xchat.utils.getChannelData


class Channel(private val id: Id<ChannelData>, private val db: CoroutineDatabase) {
    suspend fun data() = db.getChannelData(id)
}
