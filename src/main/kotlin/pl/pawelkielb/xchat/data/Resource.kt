package pl.pawelkielb.xchat.data

import pl.pawelkielb.xchat.access_control.AccessRule
import kotlin.reflect.KClass

data class Resource<T : Any>(
    val type: KClass<T>,
    val accessRule: AccessRule<T>
)
