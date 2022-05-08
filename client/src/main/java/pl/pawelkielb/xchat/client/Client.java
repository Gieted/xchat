package pl.pawelkielb.xchat.client;

import kotlin.NotImplementedError;
import pl.pawelkielb.xchat.client.config.ClientConfig;
import pl.pawelkielb.xchat.client.exceptions.FileReadException;
import pl.pawelkielb.xchat.client.exceptions.FileWriteException;
import pl.pawelkielb.xchat.data.Message;
import pl.pawelkielb.xchat.data.Name;
import pl.pawelkielb.xchat.exceptions.DisconnectedException;
import pl.pawelkielb.xchat.exceptions.NetworkException;

import java.io.IOException;
import java.net.ProtocolException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;


/**
 * A stateful xchat client.
 */
public class Client {
    private final Database database;
    private final XChatApi api;
    private final ClientConfig clientConfig;

    public Client(Database database, XChatApi api, ClientConfig clientConfig) {
        this.database = database;
        this.api = api;
        this.clientConfig = clientConfig;
    }

    /**
     * Downloads updates from the server and applies them to the database.
     *
     * @throws NetworkException      if network fails
     * @throws ProtocolException     if the server does something unexpected
     * @throws DisconnectedException if the server disconnects
     * @throws FileWriteException    if saving updates fails
     */
    public void sync() throws ProtocolException {
        var channels = api.listChannels(Set.of(clientConfig.username()), clientConfig.lastSyncTimestamp());
        System.out.println(channels);
    }

    /**
     * @param recipient username of a user you want to chat with
     * @throws NetworkException      if network fails
     * @throws ProtocolException     if the server does something unexpected
     * @throws DisconnectedException if the server disconnects
     */
    public void createPrivateChannel(Name recipient) throws ProtocolException {
        createGroupChannel(null, List.of(recipient));
    }

    /**
     * @param name    a name of the channel. If a null is provided the name will be decided by the server.
     * @param members a list of usernames of users who should be participants of this channel
     * @throws NetworkException      if network fails
     * @throws ProtocolException     if the server does something unexpected
     * @throws DisconnectedException if the server disconnects
     */
    public void createGroupChannel(Name name, List<Name> members) throws ProtocolException {

    }

    /**
     * @param channel an uuid of a channel you want to send the message to
     * @param message a message to send
     * @throws NetworkException      if network fails
     * @throws ProtocolException     if the server does something unexpected
     * @throws DisconnectedException if the server disconnects
     */
    public void sendMessage(UUID channel, Message message) {

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
    public Iterable<Message> readMessages(UUID channel, int count) {
        throw new NotImplementedError();
    }

    public static class NotFileException extends RuntimeException {
    }

    /**
     * @param channel          an uuid of the channel you want to send the file to
     * @param path             a path of the file you want to send
     * @param progressConsumer a callback function, that'll be called to report the upload progress.
     *                         Its parameter is a value from 0.0 to 1.0.
     * @throws NetworkException      if network fails
     * @throws ProtocolException     if the server does something unexpected
     * @throws DisconnectedException if the server disconnects
     * @throws NoSuchFileException   if the path's target does not exist
     * @throws NotFileException      if the path's target is not a file
     * @throws FileReadException     if reading the file fails
     */
    public void sendFile(UUID channel, Path path, Consumer<Double> progressConsumer) throws IOException {
        throw new NotImplementedError();
    }

    /**
     * @param channel              an uuid of a channel from which you want to download the file
     * @param name                 a name of the file to download
     * @param destinationDirectory a directory the file will be saved to
     * @param progressConsumer     a callback function, that'll be called to report the download progress.
     *                             Its parameter is a value from 0.0 to 1.0.
     * @throws NetworkException       if network fails
     * @throws ProtocolException      if the server does something unexpected
     * @throws DisconnectedException  if the server disconnects
     * @throws NotDirectoryException  if the not a directory is passed as a directory
     * @throws NoSuchElementException if there is no file with such a name in the channel
     * @throws FileWriteException     if saving the file fails
     */
    public void downloadFile(UUID channel, Name name, Path destinationDirectory, Consumer<Double> progressConsumer)
            throws NotDirectoryException, ProtocolException {

        throw new NotImplementedError();
    }
}
