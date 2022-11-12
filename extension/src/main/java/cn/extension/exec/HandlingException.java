package cn.extension.exec;

/**
 * 处理异常
 *
 * @author tracy
 * @since 1.0.0
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
