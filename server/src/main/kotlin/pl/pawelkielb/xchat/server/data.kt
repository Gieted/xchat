@file:UseSerializers(UUIDSerializer::class, NameSerializer::class, InstantSerializer::class)

package pl.pawelkielb.xchat.server

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import pl.pawelkielb.xchat.InstantSerializer
import pl.pawelkielb.xchat.NameSerializer
import pl.pawelkielb.xchat.UUIDSerializer
import pl.pawelkielb.xchat.data.Message
import java.util.*


@Serializable
data class MessageMongoEntry(
    val message: Message,
    val channel: UUID,
)
