package pl.pawelkielb.xchat.model

import pl.pawelkielb.xchat.access_control.AccessRule

sealed interface Model {
    val accessRule: AccessRule<*>
}
