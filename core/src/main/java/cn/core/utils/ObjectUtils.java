package cn.core.utils;

/**
 * An util class for Object.
 *
 * @author tracy
 * @since 0.2.1
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
