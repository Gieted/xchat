package pl.pawelkielb.xchat.packets;

import pl.pawelkielb.xchat.data.Name;

import static java.util.Objects.requireNonNull;

public record LoginPacket(Name username) implements Packet {
    public LoginPacket {
        requireNonNull(username);
    }
}
