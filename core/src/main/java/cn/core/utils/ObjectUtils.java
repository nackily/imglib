package cn.core.utils;

/**
 * ObjectUtils
 *
 * @author tracy
 * @since 1.0.0
 */
public final class ObjectUtils {
    private ObjectUtils(){}

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static void excNull(Object o, String msg) {
        if (isNull(o)) {
            throw new NullPointerException(msg);
        }
    }
}
