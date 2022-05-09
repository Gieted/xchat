package pl.pawelkielb.xchat.server;

import pl.pawelkielb.xchat.Logger;
import pl.pawelkielb.xchat.Observable;
import pl.pawelkielb.xchat.data.Name;
import pl.pawelkielb.xchat.utils.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static pl.pawelkielb.xchat.Functions.c;
import static pl.pawelkielb.xchat.Functions.r;
import static pl.pawelkielb.xchat.server.TransferSettings.fileChunkSizeInBytes;


/**
 * Allows saving and loading the server's data. It's thread-safe.
 */
@Singleton
public class FileDatabase {
    private final Executor ioThreads;
    private final Path messagesDirectory;
    private final Logger logger;

    private final FileTaskQueue<UUID> fileCreationTaskQueue = new FileTaskQueue<>();
    private final FileTaskQueue<Path> fileTaskQueue = new FileTaskQueue<>();

    @Inject
    public FileDatabase(@Named("io") Executor ioThreads,
                        @Named("databaseRoot") File rootDirectory,
                        Logger logger) {

        this.ioThreads = ioThreads;
        this.messagesDirectory = rootDirectory.toPath().resolve("messages");
        this.logger = logger;
    }

    /**
     * @param channel         a channel on which the file has been sent
     * @param nameProposition a proposition of a name. If it's already taken the database will choose the new one.
     * @param file            an AsyncStream of the file bytes. The database will send a byte count as a request
     *                        and the async stream should publish the requested count of bytes to the stream.
     * @return a future resolving to a name, under which the file was saved
     */
    public CompletableFuture<String> saveFile(UUID channel, Name nameProposition, AsyncStream<Integer, byte[]> file) {
        CompletableFuture<String> future = new CompletableFuture<>();
        Path filesDirectory = messagesDirectory.resolve(channel.toString()).resolve("files");

        fileCreationTaskQueue.runSuspendWriting(channel, fileCreationTask -> ioThreads.execute(r(() -> {
            try {
                Files.createDirectories(filesDirectory);

                String fileName = nameProposition.toString();
                while (true) {
                    Path filePath = filesDirectory.resolve(fileName);
                    try {
                        var output = Files.newOutputStream(
                                filePath,
                                StandardOpenOption.CREATE_NEW,
                                StandardOpenOption.WRITE
                        );

                        final String fileNameFinal = fileName;

                        fileTaskQueue.runSuspendWriting(filePath, task -> file.subscribe(fileChunkSizeInBytes, c(nextBytes -> {
                            try {
                                output.write(nextBytes);
                                file.requestNext(fileChunkSizeInBytes);
                            } catch (Exception e) {
                                task.completeExceptionally(e);
                                future.completeExceptionally(e);
                            }
                        }), r(() -> {
                            logger.info("Saved file: " + fileNameFinal);
                            output.close();
                            task.complete(null);
                            future.complete(fileNameFinal);
                        }), c(e -> {
                            output.close();
                            task.complete(null);
                            future.completeExceptionally(e);
                        })));

                        fileCreationTask.complete(null);
                        break;
                    } catch (FileAlreadyExistsException e) {
                        fileName = StringUtils.incrementFileName(fileName);
                    }
                }
            } catch (Exception e) {
                fileCreationTask.completeExceptionally(e);
                future.completeExceptionally(e);
            }
        })));

        return future;
    }

    /**
     * @param channel a channel from which to read the file
     * @param name    a name of the file
     * @return An async stream of the file.
     * Consumer must send byte count as a request and the database will respond will the requested count of bytes.
     */
    public AsyncStream<Integer, byte[]> getFile(UUID channel, Name name) {
        Observable<Integer> producer = new Observable<>();
        Observable<byte[]> consumer = new Observable<>();
        Path path = messagesDirectory.resolve(channel.toString()).resolve("files").resolve(name.toString());

        fileCreationTaskQueue.runReading(channel, () ->
                fileTaskQueue.runSuspendReading(path, c(fileTask -> {
                    try {
                        var inputStream = Files.newInputStream(path);
                        producer.subscribe(byteCount -> {
                            try {
                                byte[] nextBytes = inputStream.readNBytes(byteCount);
                                if (nextBytes.length == 0) {
                                    producer.complete();
                                    consumer.complete();
                                } else {
                                    consumer.onNext(nextBytes);
                                }
                            } catch (IOException e) {
                                fileTask.complete(null);
                                consumer.completeWithException(e);
                            }
                        }, () -> fileTask.complete(null), e -> {
                            fileTask.complete(null);
                            consumer.completeWithException(e);
                        });
                    } catch (Exception e) {
                        fileTask.complete(null);
                        consumer.completeWithException(e);
                    }
                })));

        return new AsyncStream<>(producer, consumer);
    }

    /**
     * @param channel a channel on which the file has been sent
     * @param name    a name of the file
     * @return A future resolving to the file size
     */
    public CompletableFuture<Long> getFileSize(UUID channel, Name name) {
        CompletableFuture<Long> result = new CompletableFuture<>();
        Path path = messagesDirectory.resolve(channel.toString()).resolve("files").resolve(name.toString());

        fileCreationTaskQueue.runReading(channel, () ->
                fileTaskQueue.runSuspendReading(path, task -> ioThreads.execute(() -> {
                    try {
                        long size = Files.size(path);
                        task.complete(null);
                        result.complete(size);
                    } catch (Exception e) {
                        task.complete(null);
                        result.completeExceptionally(e);
                    }
                })));

        return result;
    }
}
