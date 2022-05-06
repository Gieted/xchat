@file:UseSerializers(UUIDSerializer::class, NameSerializer::class, InstantSerializer::class)

package pl.pawelkielb.xchat.server

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.bson.codecs.pojo.annotations.BsonId
import pl.pawelkielb.xchat.data.Name
import java.time.Instant
import java.util.*


@Serializable
data class Channel(
    @BsonId val id: UUID = UUID.randomUUID(),
    val name: Name,
    val members: Set<Name>,
    val creationTimestamp: Instant = Instant.now()
)

@Serializable
data class CreateChannelRequest(val name: Name, val members: Set<Name>)
