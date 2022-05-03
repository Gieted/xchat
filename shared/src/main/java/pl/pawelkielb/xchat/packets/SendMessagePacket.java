package pl.pawelkielb.xchat.packets;

import pl.pawelkielb.xchat.data.Message;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record SendMessagePacket(UUID channel, Message message) implements Packet {
    public SendMessagePacket {
        requireNonNull(channel);
        requireNonNull(message);
    }
}
