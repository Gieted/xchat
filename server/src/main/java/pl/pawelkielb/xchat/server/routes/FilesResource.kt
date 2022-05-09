package pl.pawelkielb.xchat.server.routes

import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import org.glassfish.jersey.media.multipart.FormDataContentDisposition
import org.glassfish.jersey.media.multipart.FormDataParam
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
    fun send(
        @PathParam("channel") channelString: String,
        @FormDataParam("file") inputStream: InputStream,
        @FormDataParam("file") fileDetail: FormDataContentDisposition
    ) {
        val channel = parseChannel(channelString)
        val filename = parseName(parameterName = "file name", fileDetail.fileName)

        filesManager.saveFile(channel, nameProposition = filename, inputStream)
    }
}
