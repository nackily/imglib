package cn.example;

import cn.core.utils.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Application
 *
 * @author tracy
 * @since 1.0.0
 */
public class Application {

    private static final Properties PROPERTIES = new Properties();

    public static void main(String[] args) throws Exception {
        loadConfig("zh");

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.println(MessageFormat.format("|=========================== task-{0} ===========================|", i + 1));
            String type = selectMenu();
            if(matchedFunctions(type)) {
                doExample(type);
            }
        }
    }

    public static String selectMenu() {
        System.out.println(MessageFormat.format(" :::: C :::: {0}", PROPERTIES.getProperty("menu.c")));
        System.out.println(MessageFormat.format(" :::: Y :::: {0}", PROPERTIES.getProperty("menu.y")));
        System.out.println(MessageFormat.format(" :::: T :::: {0}", PROPERTIES.getProperty("menu.t")));

        System.out.print(" ===>> " + PROPERTIES.getProperty("menu.tips"));

        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static boolean matchedFunctions(String type) {
        ExampleSetting[] values = ExampleSetting.values();
        List<ExampleSetting> fcs = Arrays.stream(values)
                .filter(o -> o.type.equalsIgnoreCase(type))
                .collect(Collectors.toList());
        if (fcs.isEmpty()) {
            System.out.println("【Error】not found any function of type:" + type);
            return false;
        }

        for (ExampleSetting setting : fcs) {
            String propertyKey = "function." + setting.type.toLowerCase() + "." + setting.key;
            String desc = PROPERTIES.getProperty(propertyKey);
            System.out.println(MessageFormat.format(" :::: {0} :::: {1}", setting.key, desc));
        }
        System.out.print(" ===>> " + PROPERTIES.getProperty("function.tips"));
        return true;
    }

    public static void doExample(String type) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String key = scanner.nextLine();

        ExampleSetting.Function func = ExampleSetting.getFunc(type, key);
        if (func == null) {
            String tips = PROPERTIES.getOrDefault("unknown.function", "Unknown Command.").toString();
            System.out.println("【Error】" + tips);
            return;
        }
        func.apply();
        System.out.println(" <<=== Work completed.");
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
