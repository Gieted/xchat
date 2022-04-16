package pl.pawelkielb.xchat

data class Resource(val key: String, val properties: Set<Property>) {
    data class Property(val name: String, val type: Type)

    sealed interface Type {
        data class Integer(val maxValue: Int) : Type
        data class String(val minLength: Int) : Type
    }
}
