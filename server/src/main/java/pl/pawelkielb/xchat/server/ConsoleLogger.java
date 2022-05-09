package pl.pawelkielb.xchat.server;

import pl.pawelkielb.xchat.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.Executor;


/**
 * A {@link Logger} that logs to a console. It's thread-safe.
 */
@Singleton
public class ConsoleLogger implements Logger {
    private long i = 1;
    private final TaskQueue taskQueue = new TaskQueue();
    private final Executor ioThreads;

    @Inject
    public ConsoleLogger(@Named("io") Executor ioThreads) {
        this.ioThreads = ioThreads;
    }

    @Override
    public void info(String message) {
        taskQueue.runSuspend(task -> ioThreads.execute(() -> {
            System.out.printf("%d: %s\n", i++, message);
            task.complete(null);
        }));
    }
}
