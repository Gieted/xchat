package pl.pawelkielb.xchat.server.managers

import pl.pawelkielb.xchat.data.Name
import pl.pawelkielb.xchat.server.FileDatabase
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilesManager @Inject constructor(private val fileDatabase: FileDatabase) {
    fun saveFile(channel: UUID, nameProposition: Name, inputStream: InputStream) {

    }
}
