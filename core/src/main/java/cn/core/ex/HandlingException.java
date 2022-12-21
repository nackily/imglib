package cn.core.ex;

/**
 * HandlingException is the class of those exceptions that can be thrown during
 * handle image processing task.
 *
 * @author tracy
 * @since 0.2.1
 */
public class HandlingException extends RuntimeException {

    public HandlingException(String message) {
        super(message);
    }

    public HandlingException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlingException(Throwable cause) {
        super(cause);
    }

    public HandlingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
