package pl.pawelkielb.xchat.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineDatabase

suspend inline fun <reified T : Any> CoroutineDatabase.list(
    collection: String,
    filters: Set<Bson>,
    page: Int,
    pageSize: Int,
    sortCriteria: Bson
) = withContext(Dispatchers.IO) {
    val db = this@list

    db
        .getCollection<T>(collection)
        .find(*filters.toTypedArray())
        .skip(page * pageSize)
        .limit(pageSize)
        .sort(sortCriteria)
        .toList()
}

suspend inline fun <reified T : Any> CoroutineDatabase.create(collection: String, data: T) =
    withContext(Dispatchers.IO) {
        val db = this@create
        db.getCollection<T>(collection).insertOne(data)
    }
