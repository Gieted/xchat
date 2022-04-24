@file:OptIn(ExperimentalSerializationApi::class)

package pl.pawelkielb.xchat

import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

sealed interface Data {
    val id: Id<*>
}

@Serializable
data class UserData(
    @BsonId @Contextual override val id: Id<UserData> = newId(),
    var name: String
) : Data

@Serializable
data class ChannelData(
    @BsonId @Contextual override val id: Id<ChannelData> = newId(),
    var name: String,
    @EncodeDefault val members: MutableList<@Contextual Id<UserData>> = mutableListOf()
) : Data

@Serializable
data class MessageData(
    @BsonId @Contextual override val id: Id<MessageData> = newId(),
    var content: String,
    @Contextual var author: Id<UserData>
) : Data
