package cn.example;

/**
 * 功能设定
 *
 * @author tracy
 * @since 1.0.0
 */
public enum FunctionSetting {

    /**
     * 生成透明背景图像
     */
    GENERATE_TRANSPARENT_IMG("C", "1", CaptorExample::generateTransparentImg),

    /**
     * 生成纯色背景图像
     */
    GENERATE_MONO_COLOR_IMG("C", "2", CaptorExample::generateMonoColorImg),

    /**
     * 截取屏幕图像
     */
    GENERATE_SCREENSHOT("C", "3", CaptorExample::generateScreenshot),

    /**
     * 生成哈希图像
     */
    GENERATE_HASH_IMG("C", "4", CaptorExample::generateHashImg),

    /**
     * 从PDF文件中提取图像
     */
    EXTRACT_IMG_FROM_PDF("C", "5", CaptorExample::extractImgFromPdf)
    ;

    final String type;
    final String key;
    final Function func;
    FunctionSetting(String type, String key, Function func) {
        this.type = type;
        this.key = key;
        this.func = func;
    }

    public static Function getFunc(String type, String key) {
        FunctionSetting[] values = FunctionSetting.values();
        for (FunctionSetting val : values) {
            if (val.type.equalsIgnoreCase(type) && val.key.equals(key)) {
                return val.func;
            }
        }
        return null;
    }

    @FunctionalInterface
    public interface Function {
        void apply() throws Exception;
    }
}