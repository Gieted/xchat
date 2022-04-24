package pl.pawelkielb.xchat.model

import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.access_control.channelsAccessRule
import pl.pawelkielb.xchat.ChannelData
import pl.pawelkielb.xchat.utils.getChannelData


class Channel(val id: Id<ChannelData>, private val db: CoroutineDatabase): Model {
    override val accessRule = channelsAccessRule
    
    suspend fun data() = db.getChannelData(id)
}
