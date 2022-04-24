package pl.pawelkielb.xchat.access_control

import pl.pawelkielb.xchat.data.*
import pl.pawelkielb.xchat.model.Channel
import pl.pawelkielb.xchat.model.User

private val usersAccessRule: AccessRule<UserData> = { accessingUser, accessedUser ->

}

private val channelsAccessRule: AccessRule<Channel> = { accessingUser, channel ->
    if (accessingUser.isAuthenticated) {
        grantAccess(Permission.Create)

        if (channel in accessingUser!!.getChannels()) {
            grantAccess(Permission.Read, Permission.Modify)
        }
    }
}

private val messagesAccessRule: AccessRule<MessageData> = { participant, message ->

}

private val User?.isAuthenticated: Boolean
    get() = this != null
