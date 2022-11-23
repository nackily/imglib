package cn.example;

import cn.captor.gen.MonoColorImageGenerator;
import cn.captor.gen.TransparentImageGenerator;
import cn.core.utils.ColorUtils;
import cn.example.utils.ExampleUtils;
import cn.usage.Captors;

import java.io.IOException;

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

    public static void generateScreenshot() {

    }

    public static void generateHashImg() {

    }

    public static void extractImgFromPdf() {

    }
}
