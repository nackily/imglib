package cn.example;

/**
 * Setting
 *
 * @author tracy
 * @since 1.0.0
 */
public enum Setting {

    GENERATE_TRANSPARENT_IMG("C", "001", CaptorExample::generateTransparentImg);

    String type;
    String key;
    Function func;
    Setting(String type, String key, Function func) {
        this.type = type;
        this.key = key;
        this.func = func;
    }

    public static Function getFunc(String type, String key) {
        Setting[] values = Setting.values();
        for (Setting val : values) {
            if (val.type.equals(type) && val.key.equals(key)) {
                return val.func;
            }
        }
        return null;
    }

    @FunctionalInterface
    public interface Function {
        void apply();
    }
}