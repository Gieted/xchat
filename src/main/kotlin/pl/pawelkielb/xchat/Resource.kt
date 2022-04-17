package pl.pawelkielb.xchat

data class Resource(val key: String, val identifier: String, val properties: Set<Property>) {
    data class Property(val name: String, val type: Type) {
        sealed interface Type {
            data class Integer(val maxValue: Int = Int.MAX_VALUE) : Type
            data class String(val minLength: Int = 0) : Type
            data class Object(val properties: Set<Property>)
            data class Reference(val referencedKey: String)
        }
    }
}
