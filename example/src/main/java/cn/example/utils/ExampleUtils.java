package cn.example.utils;

import cn.core.utils.StringUtils;
import java.io.File;

/**
 * ExampleUtils
 *
 * @author tracy
 * @since 1.0.0
 */
public class ExampleUtils {
    private ExampleUtils(){}

    public static File tmpFileOf(String filepath) {
        return new File(tmpFileNameOf(filepath));
    }

    public static String tmpFileNameOf(String filepath) {
        if (StringUtils.isEmpty(filepath)) {
            throw new NullPointerException("file path is null");
        }

        String projectRootPath = System.getProperty("user.dir");
        String resourceRootPath = projectRootPath + "/example/res";

        return filepath.startsWith("/")
                ? (resourceRootPath + filepath)
                : (resourceRootPath + "/" + filepath);
    }
}
