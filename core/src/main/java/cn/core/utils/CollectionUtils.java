package cn.core.utils;

import cn.core.exec.InvalidSettingException;
import java.util.Collection;
import java.util.Iterator;

/**
 * CollectionUtils
 *
 * @author tracy
 * @since 1.0.0
 */
public final class CollectionUtils {
    private CollectionUtils(){}

    public static boolean isNullOrEmpty(Collection<?> col) {
        return col == null || col.size() == 0;
    }

    public static boolean isNullOrEmpty(Object[] os) {
        return os == null || os.length == 0;
    }

    public static void excNull(Object o, String msg) {
        if (o == null) {
            throw new NullPointerException(msg);
        }
    }

    public static void excEmpty(Collection<?> col, String msg) {
        if (col.size() == 0) {
            throw new InvalidSettingException(msg);
        }
    }

    public static void excEmpty(Object[] os, String msg) {
        if (os.length == 0) {
            throw new InvalidSettingException(msg);
        }
    }

    public static void excEmpty(Iterable<?> is, String msg) {
        Iterator<?> ite = is.iterator();
        if (!ite.hasNext()) {
            throw new InvalidSettingException(msg);
        }
    }

}
