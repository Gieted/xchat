package pl.pawelkielb.xchat.access_control

import pl.pawelkielb.xchat.data.*
import pl.pawelkielb.xchat.model.Channel
import pl.pawelkielb.xchat.model.User

typealias AccessRule<T> = suspend AccessControl.AccessRuleScope.(User?, T) -> Unit

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

val accessRules = mapOf(
    users to usersAccessRule,
    channels to channelsAccessRule,
    messages to messagesAccessRule
)

private val User?.isAuthenticated: Boolean
    get() = this != null
