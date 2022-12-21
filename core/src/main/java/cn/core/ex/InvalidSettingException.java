package cn.core.ex;


/**
 * InvalidSettingException is the class of those exceptions that can be thrown when
 * found any invalid setting.
 *
 * @author tracy
 * @since 0.2.1
 */
public class InvalidSettingException extends RuntimeException {

    public InvalidSettingException(String message) {
        super(message);
    }

    public InvalidSettingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSettingException(Throwable cause) {
        super(cause);
    }

    public InvalidSettingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
