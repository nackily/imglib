package cn.extension.exec;


/**
 * 不合法的设置项异常
 *
 * @author tracy
 * @since 1.0.0
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
