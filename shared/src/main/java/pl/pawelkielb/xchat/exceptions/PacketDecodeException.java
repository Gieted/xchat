package pl.pawelkielb.xchat.exceptions;

public class PacketDecodeException extends RuntimeException {
    public PacketDecodeException(String message) {
        super(message);
    }
}
