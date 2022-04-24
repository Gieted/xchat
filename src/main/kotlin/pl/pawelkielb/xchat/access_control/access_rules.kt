package pl.pawelkielb.xchat.access_control

import pl.pawelkielb.xchat.MessageData
import pl.pawelkielb.xchat.UserData
import pl.pawelkielb.xchat.model.Channel
import pl.pawelkielb.xchat.model.User

val usersAccessRule: AccessRule<UserData> = { accessingUser, accessedUser ->

}

val channelsAccessRule: AccessRule<Channel> = { accessingUser, channel ->
    if (accessingUser.isAuthenticated) {
        grantAccess(Permission.Create)

        if (channel in accessingUser!!.getChannels()) {
            grantAccess(Permission.Read, Permission.Modify)
        }
    }
}

val messagesAccessRule: AccessRule<MessageData> = { participant, message ->

}

private val User?.isAuthenticated: Boolean
    get() = this != null
