package cn.example;

import cn.captor.gen.HashImageGenerator;
import cn.captor.gen.MonoColorImageGenerator;
import cn.core.utils.ColorUtils;
import cn.usage.Captors;

import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Application
 *
 * @author tracy
 * @since 1.0.0
 */
public class Application {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, AWTException {
        long l1 = System.currentTimeMillis();

        Captors.ofEmpty()
                .addLast(new HashImageGenerator.Builder("oooo").build())
//                .addLast(new MonoColorImageGenerator.Builder().width(200).height(200).color(ColorUtils.random()).build())
                .formatName("png")
                .toFile("C:\\Users\\Coder\\Desktop\\新建文件夹\\1.png");

        System.out.println(System.currentTimeMillis() - l1);
    }

}
