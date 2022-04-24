package pl.pawelkielb.xchat.data

import pl.pawelkielb.xchat.access_control.Permission

val users = Resource(
    type = UserData::class,
    accessRule = { accessingUser, accessedUser ->
        grantAccess(Permission.Create)
    }
)

val messages = Resource(
    type = MessageData::class,
    accessRule = { _, _ ->
        
    }
)

val channels =  Resource(
    type = ChannelData::class,
    accessRule = { _, _ ->
        
    }
)

val resources = setOf(users, messages, channels)

// /channels/xd/messages
