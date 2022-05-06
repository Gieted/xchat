package pl.pawelkielb.xchat.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineDatabase
import kotlin.reflect.KProperty

suspend inline fun <reified T : Any> CoroutineDatabase.list(
    collection: String,
    filters: Set<Bson>,
    page: Int,
    pageSize: Int,
    descendingSortBy: KProperty<*>? = null,
    ascendingSortBy: KProperty<*>? = null
) = withContext(Dispatchers.IO) {
    val db = this@list

    require(descendingSortBy == null || ascendingSortBy == null) { "Cannot sort both descending and ascending" }

    db
        .getCollection<T>(collection)
        .find(*filters.toTypedArray())
        .skip(page * pageSize)
        .limit(pageSize)
        .also {
            if (descendingSortBy != null)
                it.descendingSort(descendingSortBy)
            if (ascendingSortBy != null)
                it.ascendingSort(ascendingSortBy)
        }
        .toList()
}

suspend inline fun <reified T : Any> CoroutineDatabase.create(collection: String, data: T) =
    withContext(Dispatchers.IO) {
        val db = this@create
        db.getCollection<T>(collection).insertOne(data)
    }
