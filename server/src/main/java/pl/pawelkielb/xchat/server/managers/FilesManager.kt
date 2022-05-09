package pl.pawelkielb.xchat.server.managers

import pl.pawelkielb.xchat.data.Name
import pl.pawelkielb.xchat.server.AsyncStream
import pl.pawelkielb.xchat.server.FileDatabase
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class FilesManager @Inject constructor(private val fileDatabase: FileDatabase) {
    fun saveFile(channel: UUID, nameProposition: Name, file: AsyncStream<Int, ByteArray>) {
        fileDatabase.saveFile(channel, nameProposition, file)
    }

    fun readFile(channel: UUID, name: Name): AsyncStream<Int, ByteArray> =
        fileDatabase.getFile(channel, name)

    suspend fun getFileSize(channel: UUID, name: Name): Long =
        suspendCoroutine { continuation ->
            fileDatabase.getFileSize(channel, name).thenAccept { size ->
                continuation.resume(size)
            }
        }
}
