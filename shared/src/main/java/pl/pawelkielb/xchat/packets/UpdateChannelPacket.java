package pl.pawelkielb.xchat.packets;

import pl.pawelkielb.xchat.data.Name;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record UpdateChannelPacket(UUID channel, Name name, List<Name> members) implements Packet {
    public UpdateChannelPacket {
        requireNonNull(channel);
        requireNonNull(members);
    }
}
