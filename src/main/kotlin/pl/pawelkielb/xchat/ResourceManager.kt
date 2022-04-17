package pl.pawelkielb.xchat

import javax.inject.Inject

class ResourceManager @Inject constructor() {
    fun list(key: String): List<Any> {
        return emptyList()
    }
}
