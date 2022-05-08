package pl.pawelkielb.xchat.client.config;

import pl.pawelkielb.xchat.data.Name;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public record ClientConfig(Name username, String serverHost, int serverPort, Instant lastSyncTimestamp) {
    public ClientConfig {
        requireNonNull(username);
        requireNonNull(serverHost);
        if (serverPort < 0) {
            throw new IllegalArgumentException("The server port must be a positive integer");
        }
    }

    public static ClientConfig defaults() {
        return new ClientConfig(Name.of("Guest"), "localhost", 1337, Instant.EPOCH);
    }
}
