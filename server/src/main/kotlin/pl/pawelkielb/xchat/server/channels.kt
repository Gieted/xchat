package pl.pawelkielb.xchat.server

import pl.pawelkielb.xchat.data.Channel
import pl.pawelkielb.xchat.data.Name


fun ChannelMongoEntry.toChannel(accessingUser: Name) = Channel(
    _id,
    name ?: getSyntheticChannelName(this, accessingUser),
    members,
    creationTimestamp
)

fun getSyntheticChannelName(channel: ChannelMongoEntry, accessingUser: Name): Name =
    if (channel.members.size == 2) {
        channel.members.find { it != accessingUser }!!
    } else {
        Name.of("Group channel")
    }
