package cn.core.ex;

import java.io.IOException;

/**
 * UnsupportedFormatException
 *
 * @author tracy
 * @since 1.0.0
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
