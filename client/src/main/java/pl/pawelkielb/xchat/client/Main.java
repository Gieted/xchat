package pl.pawelkielb.xchat.client;

import pl.pawelkielb.xchat.client.config.ChannelConfig;
import pl.pawelkielb.xchat.client.config.ClientConfig;
import pl.pawelkielb.xchat.client.exceptions.ExceptionHandler;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static pl.pawelkielb.xchat.client.Database.readChannelConfig;


public class Main {
    public static final boolean DEV_MODE = System.getenv("DEV_MODE") != null;

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println(
                        """
                                commands:
                                  xchat init
                                  xchat create
                                  xchat priv
                                  xchat send
                                  xchat read
                                  xchat sync
                                  xchat sendfile
                                  xchat download"""
                );
                System.exit(0);
            }

            Database database = new Database(Paths.get("."));
            ChannelConfig channelConfig = null;
            ClientConfig clientConfig = getClientConfigCatching(database);

            if (clientConfig == null) {
                try {
                    channelConfig = readChannelConfig(Paths.get(Database.channelProperties));
                } catch (Database.InvalidConfigException e) {
                    ExceptionHandler.onInvalidChannelConfig(e);
                }

                // don't change database root when channel.properties is not present in current directory
                if (channelConfig != null) {
                    database = new Database(Paths.get(".."));
                    clientConfig = getClientConfigCatching(database);
                }
            }

            Console console = new Console();
            Observable<Void> applicationExitEvent = new Observable<>();

            String command = args[0];
            List<String> commandArguments = Arrays.asList(args).subList(1, args.length);
            Commands.execute(
                    command,
                    commandArguments,
                    clientConfig,
                    channelConfig,
                    console,
                    database,
                    applicationExitEvent
            );

            applicationExitEvent.onNext(null);
        } catch (Exception e) {
            ExceptionHandler.onUnknownException(e);
        }
    }

    private static ClientConfig getClientConfigCatching(Database database) {
        try {
            return database.loadClientConfig();
        } catch (Database.InvalidConfigException e) {
            ExceptionHandler.onInvalidClientConfig(e);

            // reaching this line is impossible because ExceptionHandler methods call System.exit()
            throw new AssertionError();
        }
    }
}
