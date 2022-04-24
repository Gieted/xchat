package pl.pawelkielb.xchat.utils

import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.ChannelData

suspend inline fun <reified T : Any> CoroutineDatabase.getDocument(collection: String, documentId: Id<*>): T =
    getCollection<T>(collection)
        .find("""{_id: ObjectId("$documentId")}""")
        .first() ?: throw NoSuchElementException("No such document: $documentId")

suspend fun CoroutineDatabase.getChannelData(id: Id<ChannelData>) = getDocument<ChannelData>("channels", id)
