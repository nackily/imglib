package cn.example;

import cn.core.utils.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.Scanner;

/**
 * Application
 *
 * @author tracy
 * @since 1.0.0
 */
public class Application {

    private static final Properties PROPERTIES = new Properties();

    public static void main(String[] args) throws Exception {
        loadConfig("");
        while (true) {
            System.out.println("|===========================MENUS===========================|");
            printMenus();
            String type = selectMenu();
            printFunctions(type);
            String key = selectFunction();
            doExample(type, key);
            System.out.println(" <<=== Work completed.");
        }
    }

    public static void printMenus() {
        System.out.println(MessageFormat.format(" :::: C :::: {0}", PROPERTIES.getProperty("menu.c")));
        System.out.println(MessageFormat.format(" :::: Y :::: {0}", PROPERTIES.getProperty("menu.y")));
        System.out.println(MessageFormat.format(" :::: T :::: {0}", PROPERTIES.getProperty("menu.t")));

        System.out.print(" ===>> " + PROPERTIES.getProperty("menu.tips"));
    }

    public static String selectMenu() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static void printFunctions(String type) {
        ExampleSetting[] values = ExampleSetting.values();
        for (ExampleSetting setting : values) {
            if (setting.type.equals(type)) {
                String propertyKey = "function." + setting.type.toLowerCase() + "." + setting.key;
                String desc = PROPERTIES.getProperty(propertyKey);
                System.out.println(MessageFormat.format(" :::: {0} :::: {1}", setting.key, desc));
            }
        }

        System.out.print(" ===>> " + PROPERTIES.getProperty("function.tips"));
    }

    public static String selectFunction() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static void doExample(String type, String key) throws Exception {
        ExampleSetting.Function func = ExampleSetting.getFunc(type, key);
        if (func == null) {
            System.out.println(PROPERTIES.getOrDefault("unknown.function", "Unknown Command."));
            return;
        }
        func.apply();
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
