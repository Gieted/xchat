package pl.pawelkielb.xchat.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import pl.pawelkielb.xchat.client.config.ChannelConfig
import pl.pawelkielb.xchat.client.config.ClientConfig
import pl.pawelkielb.xchat.client.exceptions.DisconnectedException
import pl.pawelkielb.xchat.client.exceptions.FileReadException
import pl.pawelkielb.xchat.client.exceptions.FileWriteException
import pl.pawelkielb.xchat.client.exceptions.NetworkException
import pl.pawelkielb.xchat.data.Message
import pl.pawelkielb.xchat.data.Name
import pl.pawelkielb.xchat.data.SendMessageRequest
import pl.pawelkielb.xchat.maxPageSize
import pl.pawelkielb.xchat.utils.StringUtils
import java.io.IOException
import java.io.OutputStream
import java.net.ProtocolException
import java.nio.file.*
import java.time.Instant
import java.util.*
import java.util.function.Consumer
import kotlin.io.path.exists
import kotlin.io.path.fileSize
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.math.min


/**
 * A stateful xchat client.
 */
class Client(private val database: Database, private val api: XChatApi, private val clientConfig: ClientConfig) {
    /**
     * Downloads updates from the server and applies them to the database.
     *
     * @throws NetworkException      if network fails
     * @throws ProtocolException     if the server does something unexpected
     * @throws DisconnectedException if the server disconnects
     * @throws FileWriteException    if saving updates fails
     */
    @Throws(ProtocolException::class)
    fun sync() = runBlocking {
        val lastSyncTimestamp = database.loadCache().lastSyncTimestamp
        fetchAll(
            generate = { page, pageSize ->
                api.listChannels(setOf(clientConfig.username()), lastSyncTimestamp, page, pageSize)
            },
            consume = { channels ->
                for (channel in channels) {
                    database.saveChannel(channel.name, ChannelConfig(channel.id))
                }
            })
        updateLastSyncTimestamp(database)
    }

    /**
     * @param recipient username of a user you want to chat with
     * @throws NetworkException      if network fails
     * @throws ProtocolException     if the server does something unexpected
     * @throws DisconnectedException if the server disconnects
     */
    @Throws(ProtocolException::class)
    fun createPrivateChannel(recipient: Name) = createGroupChannel(null, setOf(recipient))

    /**
     * @param name    a name of the channel. If a null is provided the name will be decided by the server.
     * @param members a set of usernames of users who should be participants of this channel
     * @throws NetworkException      if network fails
     * @throws ProtocolException     if the server does something unexpected
     * @throws DisconnectedException if the server disconnects
     */
    @Throws(ProtocolException::class)
    fun createGroupChannel(name: Name?, members: Set<Name>) = runBlocking {
        api.createChannel(name, members + clientConfig.username)
        sync()
    }

    /**
     * @param channel an uuid of a channel you want to send the message to
     * @param content a content of a message to send
     * @throws NetworkException      if network fails
     * @throws ProtocolException     if the server does something unexpected
     * @throws DisconnectedException if the server disconnects
     */
    fun sendMessage(channel: UUID, content: String) = runBlocking {
        api.sendMessage(channel, SendMessageRequest(content))
    }

    /**
     * @param channel an uuid of a channel from which you want to read the messages
     * @param count   a count of messages to read
     * @return an iterable of read messages.
     * Calling it's next() and hasNext() might produce the same exceptions as this method call.
     * @throws NetworkException      if network fails
     * @throws ProtocolException     if the server does something unexpected
     * @throws DisconnectedException if the server disconnects
     */
    fun readMessages(channel: UUID, count: Int): Iterable<Message> = runBlocking {
        val now = Instant.now()
        var page = 0
        var messagesLeft = count
        val iterator = object : Iterator<Message> {
            val messagesQueue: Queue<Message> = LinkedList()
            var hasRemoteMessages = true

            init {
                fetchMoreMessages()
            }

            fun fetchMoreMessages() = runBlocking {
                val messagesToFetch = min(messagesLeft, maxPageSize)
                val newMessages = api.listMessages(channel, now, page++, messagesToFetch)
                messagesLeft -= newMessages.size
                if (newMessages.size != messagesToFetch || messagesLeft == 0) {
                    hasRemoteMessages = false
                }
                messagesQueue.addAll(newMessages.reversed())
            }

            override fun hasNext(): Boolean {
                if (messagesQueue.isEmpty() && hasRemoteMessages) {
                    fetchMoreMessages()
                }

                return messagesQueue.isNotEmpty()
            }

            override fun next(): Message {
                if (!hasNext()) {
                    throw NoSuchElementException()
                }
                if (messagesQueue.isEmpty()) {
                    fetchMoreMessages()
                }

                return messagesQueue.poll()
            }
        }

        Iterable { iterator }
    }

    class NotFileException : RuntimeException()

    /**
     * @param channel          an uuid of the channel you want to send the file to
     * @param path             a path of the file you want to send
     * @param progressConsumer a callback function, that'll be called to report the upload progress.
     * Its parameter is a value from 0.0 to 1.0.
     * @throws NetworkException      if network fails
     * @throws ProtocolException     if the server does something unexpected
     * @throws DisconnectedException if the server disconnects
     * @throws NoSuchFileException   if the path's target does not exist
     * @throws NotFileException      if the path's target is not a file
     * @throws FileReadException     if reading the file fails
     */
    @Throws(IOException::class)
    fun sendFile(channel: UUID, path: Path, progressConsumer: Consumer<Double>) = runBlocking {
        if (!path.exists()) {
            throw NoSuchFileException(path.toFile())
        }

        if (!path.isRegularFile()) {
            throw NotFileException()
        }

        val file = path.toFile().inputStream()
        val name = Name.of(path.fileName.toString())
        val size = path.fileSize()

        api.uploadFile(channel, file, name, size, progressConsumer)
    }

    /**
     * @param channel              an uuid of a channel from which you want to download the file
     * @param name                 a name of the file to download
     * @param destinationDirectory a directory the file will be saved to
     * @param progressConsumer     a callback function, that'll be called to report the download progress.
     * Its parameter is a value from 0.0 to 1.0.
     * @throws NetworkException       if network fails
     * @throws ProtocolException      if the server does something unexpected
     * @throws DisconnectedException  if the server disconnects
     * @throws NotDirectoryException  if the not a directory is passed as a directory
     * @throws NoSuchElementException if there is no file with such a name in the channel
     * @throws FileWriteException     if saving the file fails
     */
    @Throws(NotDirectoryException::class, ProtocolException::class)
    fun downloadFile(channel: UUID, name: Name, destinationDirectory: Path, progressConsumer: Consumer<Double>) =
        runBlocking {
            if (!destinationDirectory.isDirectory()) {
                throw NotDirectoryException(destinationDirectory.toString())
            }

            var filename = name.toString()
            var filePath: Path
            var output: OutputStream? = null

            // find unused file name
            while (output == null) {
                filePath = destinationDirectory.resolve(filename)
                try {
                    output = withContext(Dispatchers.IO) {
                        Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)
                    }
                } catch (e: FileAlreadyExistsException) {
                    filename = StringUtils.incrementFileName(filename)
                } catch (e: IOException) {
                    throw FileWriteException(filePath, e)
                }
            }

            api.downloadFile(channel, name, output, progressConsumer)
        }
}
