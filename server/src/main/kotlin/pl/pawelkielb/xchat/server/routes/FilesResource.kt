package pl.pawelkielb.xchat.server.routes

import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import org.glassfish.jersey.media.multipart.FormDataContentDisposition
import org.glassfish.jersey.media.multipart.FormDataParam
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Path("/channels/{channel}/files")
class FilesResource @Inject constructor() {
    @POST
    fun send(
        @FormDataParam("file") uploadedInputStream: InputStream,
        @FormDataParam("file") fileDetail: FormDataContentDisposition
    ) {
        TODO("not implemented")
    }
}
