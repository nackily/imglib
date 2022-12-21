package cn.core.utils;

import cn.core.ex.InvalidSettingException;
import java.util.Collection;
import java.util.Iterator;

/**
 * An util class for collection。
 *
 * @author tracy
 * @since 0.2.1
 */
public final class CollectionUtils {
    private CollectionUtils(){}

    public static boolean isNullOrEmpty(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    public static boolean isNullOrEmpty(Object[] os) {
        return os == null || os.length == 0;
    }

    public static void excEmpty(Collection<?> col, String msg) {
        if (col.isEmpty()) {
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
