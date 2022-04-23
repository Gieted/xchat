package pl.pawelkielb.xchat.data

import pl.pawelkielb.xchat.resource

val users = resource(identifier = UserData::id)
val messages = resource(identifier = MessageData::id)
val channels = resource(identifier = ChannelData::id, children = setOf(messages))

val topLevelResources = mapOf(
    "users" to users,
    "channels" to channels
)
