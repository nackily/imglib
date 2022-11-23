package cn.example;

import cn.captor.gen.HashImageGenerator;
import cn.captor.gen.MonoColorImageGenerator;
import cn.captor.gen.ScreenshotGenerator;
import cn.captor.gen.TransparentImageGenerator;
import cn.core.utils.ColorUtils;
import cn.example.utils.ExampleUtils;
import cn.usage.Captors;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * CaptorExample
 *
 * @author tracy
 * @since 1.0.0
 */
public class CaptorExample {

    public static void generateTransparentImg() throws IOException {
        Captors.ofEmptySource()
                .addLast(new TransparentImageGenerator.Builder()
                        .width(300)
                        .height(200)
                        .build())
                .toFile(ExampleUtils.tmpFileNameOf("out/gen_transparent.png"));
    }

    public static void generateMonoColorImg() throws IOException {
        Captors.ofEmptySource()
                .addLast(new MonoColorImageGenerator.Builder()
                        .width(300)
                        .height(200)
                        .alpha(0.2f)
                        .color(ColorUtils.random())
                        .build())
                .toFile(ExampleUtils.tmpFileNameOf("out/gen_mono_color.png"));
    }

    public static void generateScreenshot() throws IOException {
        Captors.ofEmptySource()
                .addLast(new ScreenshotGenerator.Builder()
                        .startPoint(600, 300)
                        .width(280)
                        .height(190)
                        .build())
                .toFile(ExampleUtils.tmpFileNameOf("out/gen_screenshot.png"));
    }

    public static void generateHashImg() throws IOException, NoSuchAlgorithmException {
        Captors.ofEmptySource().addLast(new HashImageGenerator.Builder("imglib-user")
                .gridVerticalNum(5)
                .fgColor(ColorUtils.of(50, 150, 50))
                .build())
                .toFile(ExampleUtils.tmpFileNameOf("out/hash.png"));
    }

    public static void extractImgFromPdf() throws IOException {
        Captors.ofPdf(ExampleUtils.tmpFileNameOf("in/jvms8.pdf"))
                .page(0)
                .dpi(280)
                .toFile(ExampleUtils.tmpFileNameOf("out/page_1_of_jvms8.jpg"));
    }
}
