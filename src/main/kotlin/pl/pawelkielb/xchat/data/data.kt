package pl.pawelkielb.xchat.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId


@Serializable
data class UserData(
    @BsonId @Contextual val id: Id<UserData> = newId(),
    var name: String
)

@Serializable
data class ChannelData(
    @BsonId @Contextual val id: Id<ChannelData> = newId(),
    var name: String,
    val members: MutableList<@Contextual UserData> = mutableListOf()
)

@Serializable
data class MessageData(
    @BsonId @Contextual val id: Id<MessageData> = newId(),
    var content: String,
    @Contextual var author: Id<UserData>
)
