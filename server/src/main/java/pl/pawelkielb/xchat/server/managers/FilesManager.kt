package pl.pawelkielb.xchat.server.managers

import pl.pawelkielb.xchat.Observable
import pl.pawelkielb.xchat.data.Name
import pl.pawelkielb.xchat.server.AsyncStream
import pl.pawelkielb.xchat.server.FileDatabase
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilesManager @Inject constructor(private val fileDatabase: FileDatabase) {
    @Suppress("RedundantSuspendModifier")
    suspend fun saveFile(channel: UUID, nameProposition: Name, inputStream: InputStream) {
        val producer = Observable<Int>()
        val consumer = Observable<ByteArray>()
        val asyncStream = AsyncStream(producer, consumer)

        producer.subscribe { bytesCount ->
            @Suppress("BlockingMethodInNonBlockingContext")
            val bytes = inputStream.readNBytes(bytesCount)
            consumer.onNext(bytes)
            if (bytes.size != bytesCount) {
                consumer.complete()
            }
        }

        fileDatabase.saveFile(channel, nameProposition, asyncStream)
    }
}
