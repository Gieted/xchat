@file:UseSerializers(UUIDSerializer::class, NameSerializer::class, InstantSerializer::class)

package pl.pawelkielb.xchat.client

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.pawelkielb.xchat.InstantSerializer
import pl.pawelkielb.xchat.NameSerializer
import pl.pawelkielb.xchat.UUIDSerializer
import java.io.File
import java.time.Instant


@Serializable
data class Cache(var lastSyncTimestamp: Instant = Instant.EPOCH)


fun loadCache(file: File): Cache {
    val json = file.readText()
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
