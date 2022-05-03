@file:UseSerializers(UUIDSerializer::class, NameSerializer::class)

package pl.pawelkielb.xchat.server

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.bson.codecs.pojo.annotations.BsonId
import pl.pawelkielb.xchat.data.Name
import java.util.*


@Serializable
data class Channel(
    @BsonId val id: UUID = UUID.randomUUID(),
    val name: Name,
    val members: Set<Name>
)

@Serializable
data class CreateChannelRequest(val name: Name, val members: Set<Name>)
