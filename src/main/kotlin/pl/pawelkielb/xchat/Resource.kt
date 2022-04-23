package pl.pawelkielb.xchat

import kotlin.reflect.KClass

data class Resource(
    val identifier: Property,
    val properties: Set<Property>,
    val children: Map<String, Resource>,
    val type: KClass<*>
) {
    data class Property(val name: String, val type: Type) {
        sealed interface Type {
            data class Integer(val maxValue: Int = Int.MAX_VALUE) : Type
            data class String(val minLength: Int = 0) : Type
            data class Object(val properties: Set<Property>)
        }
    }
}
