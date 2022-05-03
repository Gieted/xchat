package pl.pawelkielb.xchat.packets;

import pl.pawelkielb.xchat.data.Name;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record ChannelUpdatedPacket(UUID channel, Name name) implements Packet {
    public ChannelUpdatedPacket {
        requireNonNull(channel);
        requireNonNull(name);
    }
}
