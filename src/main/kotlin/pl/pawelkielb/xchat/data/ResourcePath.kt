package pl.pawelkielb.xchat.data

data class ResourcePathSegment(val name: String, val isVariable: Boolean) {
    init {
        if (!name.matches("""^[A-Za-z\d/]+$""".toRegex())) {
            throw IllegalArgumentException("Illegal characters were provided")
        }
    }
}

typealias ResourcePath = List<ResourcePathSegment>
