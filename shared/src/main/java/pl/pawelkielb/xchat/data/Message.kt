@file:UseSerializers(UUIDSerializer::class, NameSerializer::class, InstantSerializer::class)

package pl.pawelkielb.xchat.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import pl.pawelkielb.xchat.InstantSerializer
import pl.pawelkielb.xchat.NameSerializer
import pl.pawelkielb.xchat.UUIDSerializer
import java.time.Instant

@Serializable
data class Message @JvmOverloads constructor(
    val author: Name,
    val content: String,
    val sendingTimestamp: Instant = Instant.now()
) {
    init {
        require(content.isNotBlank()) { "Content cannot be blank" }
        require(content.length <= 10000) { "A message content cannot be longer than 10000 characters" }
    }
}
