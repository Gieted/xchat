package pl.pawelkielb.xchat

object Key {
    fun validate(key: String) {
        if (!key.startsWith("/")) {
            throw IllegalArgumentException("Key must start with a slash")
        }

        if (key.endsWith("/")) {
            throw IllegalArgumentException("Key cannot end with a slash")
        }

        if (key.contains("//")) {
            throw IllegalArgumentException("Key cannot contain double slash")
        }

        if (!key.matches("""^[A-Za-z\d/]+$""".toRegex())) {
            throw IllegalArgumentException("Illegal characters were provided")
        }
    }

    fun targetsGroup(key: String) = key.drop(1).split("/").size % 2 != 0
}
