package pl.pawelkielb.xchat.packets;

import pl.pawelkielb.xchat.data.Name;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record RequestFilePacket(UUID channel, Name name) implements Packet {
    public RequestFilePacket {
        requireNonNull(channel);
        requireNonNull(name);
    }
}
