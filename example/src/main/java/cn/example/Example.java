package cn.example;

import cn.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Example
 *
 * @author tracy
 * @since 1.0.0
 */
public class Example {
    private static final Logger logger = LoggerFactory.getLogger(Example.class);
    private static final Properties PROPERTIES = new Properties();

    public static void main(String[] args) throws Exception {

        loadConfig("zh");

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            logger.info("|=========================== task-{} ===========================|", i + 1);
            String type = selectMenu();
            if(matchedFunctions(type)) {
                doExample(type);
            }
        }
    }

    public static String selectMenu() {
        logger.info(" :::: C :::: {}", PROPERTIES.getProperty("menu.c"));
        logger.info(" :::: Y :::: {}", PROPERTIES.getProperty("menu.y"));
        logger.info(" :::: T :::: {}", PROPERTIES.getProperty("menu.t"));

        logger.info(" ===>> {}", PROPERTIES.getProperty("menu.tips"));

        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static boolean matchedFunctions(String type) {
        Setting[] values = Setting.values();
        List<Setting> fcs = Arrays.stream(values)
                .filter(o -> o.type.equalsIgnoreCase(type))
                .collect(Collectors.toList());
        if (fcs.isEmpty()) {
            logger.error("not found any function of type:{}", type);
            return false;
        }

        for (Setting setting : fcs) {
            String propertyKey = "function." + setting.type.toLowerCase() + "." + setting.key;
            String desc = PROPERTIES.getProperty(propertyKey);
            logger.info(" :::: {} :::: {}", setting.key, desc);
        }

        logger.info(" ===>> {}", PROPERTIES.getProperty("function.tips"));
        return true;
    }

    public static void doExample(String type) throws IOException, NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        String key = scanner.nextLine();

        Setting.Function func = Setting.getFunc(type, key);
        if (func == null) {
            String tips = PROPERTIES.getOrDefault("unknown.function", "Unknown Command.").toString();
            logger.error(tips);
            return;
        }
        func.apply();
        logger.info(" <<=== Work completed.");
    }

    public static void loadConfig(String language) {
        try {
            String proName = StringUtils.isEmpty(language)
                    ? "menus.properties"
                    : MessageFormat.format("menus-{0}.properties", language);
            InputStream stream = ClassLoader.getSystemResourceAsStream(proName);
            PROPERTIES.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
