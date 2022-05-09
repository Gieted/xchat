package pl.pawelkielb.xchat.server.routes

import jakarta.ws.rs.*
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.StreamingOutput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import pl.pawelkielb.xchat.TransferSettings.fileChunkSizeInBytes
import pl.pawelkielb.xchat.server.managers.FilesManager
import pl.pawelkielb.xchat.server.parseChannel
import pl.pawelkielb.xchat.server.parseName
import java.nio.file.NoSuchFileException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@Path("/channels/{channel}/files/{fileName}")
class FileResource @Inject constructor(private val filesManager: FilesManager) {
    @GET
    @Produces(MediaType.WILDCARD)
    fun download(
        @PathParam("channel") channelString: String,
        @PathParam("fileName") fileNameString: String
    ): Response = runBlocking(Dispatchers.Default) {
        val channel = parseChannel(channelString)
        val fileName = parseName(parameterName = "file name", string = fileNameString)

        val file = filesManager.readFile(channel, fileName)

        val fileSize = try {
            filesManager.getFileSize(channel, fileName)
        } catch (e: NoSuchFileException) {
            throw NotFoundException("File $fileName does not exist")
        }

        val streamingOutput = StreamingOutput { output ->
            file.subscribe(fileChunkSizeInBytes, { nextBytes ->
                output.write(nextBytes)
            }, { output.close() })
        }

        Response.ok(streamingOutput).header(HttpHeaders.CONTENT_LENGTH, fileSize).build()
    }
}
