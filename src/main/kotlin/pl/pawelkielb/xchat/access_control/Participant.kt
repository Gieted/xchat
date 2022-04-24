package pl.pawelkielb.xchat.access_control

import pl.pawelkielb.xchat.UserData

data class Participant(val user: UserData?, val isAdmin: Boolean = false) {
    val isLoggedIn
     get() = user != null
}
