package cn.core.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

/**
 * StringUtils
 *
 * @author tracy
 * @since 1.0.0
 */
public final class StringUtils {
    private StringUtils(){}
    private static final String EMPTY = "";

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String join(String[] array) {
        if (array == null) {
            return EMPTY;
        }
        return join(Arrays.asList(array));
    }

    public static String join(String[] array, Character separator) {
        if (array == null) {
            return EMPTY;
        }
        return join(Arrays.asList(array), separator);
    }

    public static String join(Collection<String> col) {
        if (col == null || col.isEmpty()) {
            return EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        join(col, ',', builder);
        return builder.toString();
    }

    public static String join(Collection<String> col, Character separator) {
        if (col == null || col.isEmpty()) {
            return EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        join(col, separator, builder);
        return builder.toString();
    }

    public static void join(Iterable<String> iterable, char separator, StringBuilder sb) {
        if (iterable == null) {
            return;
        }
        boolean first = true;
        for (String value : iterable) {
            if (first) {
                first = false;
            } else {
                sb.append(separator);
            }
            sb.append(value);
        }
    }


    /**
     * Returns the file extension of {@link File}.
     * @param f the file
     * @return the extension of the file
     */
    public static String getExtensionName(File f) {
        return getExtensionName(f.getName());
    }

    /**
     * Returns the file extension of file name
     * @param filename the file name
     * @return the extension of the file
     */
    public static String getExtensionName(String filename) {
        boolean c1 = filename.indexOf('.') != -1;
        boolean c2 = filename.lastIndexOf('.') != filename.length() - 1;
        if (c1 && c2) {
            int lastIndex = filename.lastIndexOf('.');
            return filename.substring(lastIndex + 1);
        }
        return null;
    }
}
