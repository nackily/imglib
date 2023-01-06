package cn.pipe;


import java.lang.reflect.Field;

public final class ReflectionUtils {

    public static Object get(String field, Object o) {
        try {
            Field f = o.getClass().getDeclaredField(field);
            f.setAccessible(Boolean.TRUE);
            return f.get(o);
        } catch (NoSuchFieldException e) {
            throw new AssertionError("No field as: " + field);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Illegal access field: " + field);
        }
    }

    public static Object getFromSuper(String field, Object o) {
        try {
            Field f = o.getClass().getSuperclass().getDeclaredField(field);
            f.setAccessible(Boolean.TRUE);
            return f.get(o);
        } catch (NoSuchFieldException e) {
            throw new AssertionError("No field as: " + field);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Illegal access field: " + field);
        }
    }
}
