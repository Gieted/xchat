package pl.pawelkielb.xchat.server.routes

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.glassfish.jersey.media.multipart.FormDataContentDisposition
import org.glassfish.jersey.media.multipart.FormDataParam
import pl.pawelkielb.xchat.Observable
import pl.pawelkielb.xchat.server.AsyncStream
import pl.pawelkielb.xchat.server.managers.FilesManager
import pl.pawelkielb.xchat.server.parseChannel
import pl.pawelkielb.xchat.server.parseName
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Path("/channels/{channel}/files")
class FilesResource @Inject constructor(private val filesManager: FilesManager) {
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun send(
        @PathParam("channel") channelString: String,
        @FormDataParam("file") inputStream: InputStream,
        @FormDataParam("file") fileDetail: FormDataContentDisposition
    ) = runBlocking(Dispatchers.Default) {
        val channel = parseChannel(channelString)
        val filename = parseName(parameterName = "file name", fileDetail.fileName)

        val producer = Observable<Int>()
        val consumer = Observable<ByteArray>()
        val file = AsyncStream(producer, consumer)

        producer.subscribe { requestedBytes ->
            val bytes = inputStream.readNBytes(requestedBytes)
            if (bytes.isNotEmpty()) {
                consumer.onNext(bytes)
            }

            if (bytes.size != requestedBytes) {
                consumer.complete()
            }
        }

        filesManager.saveFile(channel, nameProposition = filename, file)
    }
}
