package pl.pawelkielb.xchat

import java.util.UUID

data class User(
    val id: UUID,
    var nickname: ConstrainedString = ConstrainedString(maxLength = 25, notBlank = true),
    var channels: Set<Channel>
)

data class Channel(
    val id: UUID,
    var name: ConstrainedString = ConstrainedString(maxLength = 50, notBlank = true)
)
