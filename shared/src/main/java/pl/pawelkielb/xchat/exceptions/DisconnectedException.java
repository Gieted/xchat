package pl.pawelkielb.xchat.exceptions;

import java.io.IOException;

public class DisconnectedException extends RuntimeException {
    public DisconnectedException(IOException cause) {
        super("The server has unexpectedly disconnected", cause);
    }
}
