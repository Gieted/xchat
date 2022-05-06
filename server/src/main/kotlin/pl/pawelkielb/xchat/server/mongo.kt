package pl.pawelkielb.xchat.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.data.Channel

suspend fun CoroutineDatabase.list(collection: String, filters: Set<Bson>, page: Int, pageSize: Int) =
    withContext(Dispatchers.IO) {
        val db = this@list
        db
            .getCollection<Channel>(collection)
            .find(*filters.toTypedArray())
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
    }

suspend inline fun <reified T : Any> CoroutineDatabase.create(collection: String, data: T) =
    withContext(Dispatchers.IO) {
        val db = this@create
        db.getCollection<T>(collection).insertOne(data)
    }
