@file:UseSerializers(UUIDSerializer::class, NameSerializer::class, InstantSerializer::class)

package pl.pawelkielb.xchat.client

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import pl.pawelkielb.xchat.InstantSerializer
import pl.pawelkielb.xchat.NameSerializer
import pl.pawelkielb.xchat.UUIDSerializer
import java.io.File
import java.io.FileNotFoundException
import java.time.Instant


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Cache(@EncodeDefault var lastSyncTimestamp: Instant = Instant.EPOCH)


fun loadCache(file: File): Cache? {
    val json = try {
        file.readText()
    } catch (e: FileNotFoundException) {
        return null
    }

    return Json.decodeFromString(json)
}

fun saveCache(file: File, cache: Cache) {
    val json = Json.encodeToString(cache)
    file.writeText(json)
}

fun updateLastSyncTimestamp(database: Database) {
    val cache = database.cache
    cache.lastSyncTimestamp = Instant.now()
    database.saveCache(cache)
}
