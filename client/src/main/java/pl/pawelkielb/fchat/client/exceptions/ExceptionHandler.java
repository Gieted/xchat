package pl.pawelkielb.fchat.client.exceptions;

import pl.pawelkielb.fchat.Exceptions;
import pl.pawelkielb.fchat.client.Database;
import pl.pawelkielb.fchat.client.Main;
import pl.pawelkielb.fchat.exceptions.DisconnectedException;
import pl.pawelkielb.fchat.exceptions.NetworkException;

import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

public abstract class ExceptionHandler {
    public static void onCannotFindClientConfig() {
        checkDevMode();
        printError("Cannot find fchat.properties file. Are you in fchat's directory?");
        System.exit(2);
    }

    public static void onInvalidClientConfig(Database.InvalidConfigException e) {
        checkDevMode(e);
        printError(getExceptionMessage("Invalid client config", e));
        System.exit(3);
    }

    public static void onInvalidChannelConfig(Database.InvalidConfigException e) {
        checkDevMode(e);
        printError(getExceptionMessage("Invalid channel config", e));
        System.exit(4);
    }

    public static void onCannotReadFile(FileReadException e) {
        checkDevMode();
        printError(getExceptionMessage(e));
        System.exit(5);
    }

    public static void onCannotWriteFile(FileWriteException e) {
        checkDevMode();
        printError(getExceptionMessage(e));
        System.exit(6);
    }

    public static void onNetworkException(NetworkException e) {
        checkDevMode(e);
        printError(getExceptionMessage(e));
        System.exit(7);
    }

    public static void onServerDisconnected(DisconnectedException e) {
        checkDevMode(e);
        printError(getExceptionMessage(e));
        System.exit(8);
    }

    public static void onUnknownCommand(String command) {
        checkDevMode();
        printError("Unknown command: " + command);
        System.exit(9);
    }

    public static void onMissingArgument(String message) {
        checkDevMode();
        printError(getExceptionMessage("Missing argument", message));
        System.exit(10);
    }

    public static void onIllegalArgument(String message, Exception e) {
        checkDevMode(e);
        printError(getExceptionMessage("Illegal argument value -> " + message, e));
        System.exit(11);
    }

    public static void onIllegalArgument(String message) {
        checkDevMode();
        printError(getExceptionMessage("Illegal argument value", message));
        System.exit(11);
    }

    public static void onCommandNotUsedInChannelDirectory() {
        checkDevMode();
        printError("This command can only be used in a channel directory");
        System.exit(12);
    }

    public static void onAlreadyInitialized() {
        checkDevMode();
        printError("This is already an fchat directory");
        System.exit(13);
    }

    public static void onProtocolException(ProtocolException e) {
        checkDevMode(e);
        printError(getExceptionMessage("Protocol error", e));
        System.exit(15);
    }

    public static void onUnknownException(Exception e) {
        checkDevMode(e);
        printError(getExceptionMessage("An unknown error has happened", e));
        System.exit(100);
    }

    private static void printError(String message) {
        System.err.println();
        System.err.println(message);
        System.err.println();
    }

    private static void checkDevMode(Exception e) {
        if (Main.DEV_MODE) {
            if (e != null) {
                Exceptions.throwAsUnchecked(e);
            } else {
                throw new RuntimeException();
            }
        }
    }

    private static void checkDevMode() {
        checkDevMode(null);
    }

    private static String getExceptionMessage(String... messages) {
        return String.join(" -> ", messages);
    }

    private static String getExceptionMessage(Exception e) {
        List<String> result = new ArrayList<>();
        Throwable throwable = e;
        while (throwable != null) {
            if (throwable.getMessage() != null) {
                result.add(throwable.getMessage());
            } else {
                result.add(throwable.getClass().getSimpleName());
            }
            throwable = throwable.getCause();
        }

        return getExceptionMessage(result.toArray(new String[0]));
    }

    private static String getExceptionMessage(String message, Exception e) {
        return message + " -> " + getExceptionMessage(e);
    }
}
