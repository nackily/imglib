package cn.core.ex;

import java.io.IOException;

/**
 * Signals that an unsupported format has been found.
 *
 * @author tracy
 * @since 0.2.1
 */
public class UnsupportedFormatException extends IOException {

    public UnsupportedFormatException(String message) {
        super(message);
    }

    public UnsupportedFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedFormatException(Throwable cause) {
        super(cause);
    }
}
