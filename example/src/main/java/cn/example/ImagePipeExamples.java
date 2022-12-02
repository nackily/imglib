package cn.example;

import cn.pipe.captor.HashImageGenerator;
import cn.pipe.captor.MonoColorImageGenerator;
import cn.pipe.captor.ScreenshotGenerator;
import cn.pipe.captor.TransparentImageGenerator;
import cn.core.utils.ColorUtils;
import cn.example.utils.ExampleUtils;
import cn.pipe.out.GifFileEncoder;
import cn.pipe.ypipe.merge.GridMergeHandler;
import cn.pipe.ypipe.split.GridSplitHandler;
import cn.usage.ImagePipes;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * ImagePipeExamples
 *
 * @author tracy
 * @since 1.0.0
 */
public class ImagePipeExamples {
    private ImagePipeExamples(){}

    public static void generateTransparentImg() throws IOException {
        ImagePipes.ofEmptySource()
                .register(new TransparentImageGenerator.Builder()
                        .width(300)
                        .height(200)
                        .build())
                .toFile(ExampleUtils.tmpFileNameOf("out/transparent.png"));
    }

    public static void generateMonoColorImg() throws IOException {
        ImagePipes.ofEmptySource()
                .register(new MonoColorImageGenerator.Builder()
                        .width(300)
                        .height(200)
                        .alpha(0.2f)
                        .color(ColorUtils.random())
                        .build())
                .toFile(ExampleUtils.tmpFileNameOf("out/mono_color.png"));
    }

    public static void generateScreenshot() throws IOException {
        ImagePipes.ofEmptySource()
                .register(new ScreenshotGenerator.Builder()
                        .startPoint(600, 300)
                        .width(280)
                        .height(190)
                        .build())
                .toFile(ExampleUtils.tmpFileNameOf("out/screenshot.png"));
    }

    public static void generateHashImg() throws IOException, NoSuchAlgorithmException {
        ImagePipes.ofEmptySource()
                .register(new HashImageGenerator.Builder("imglib-user")
                        .gridVerticalNum(5)
                        .fgColor(ColorUtils.of(50, 150, 50))
                        .build())
                .toFile(ExampleUtils.tmpFileNameOf("out/hash.png"));
    }

    public static void extractImgFromPdf() throws IOException {
        ImagePipes.ofPdf(ExampleUtils.tmpFileNameOf("in/jvms8.pdf"))
                .register(0)
                .dpi(280)
                .toFile(ExampleUtils.tmpFileNameOf("out/page_1_of_jvms8.jpg"));
    }

    public static void extractImgFromGif() throws IOException {
        ImagePipes.ofGif(ExampleUtils.tmpFileNameOf("in/duck.gif"))
                .registerAll()
                .toFiles(ExampleUtils.tmpFileNameOf("out/gif/frame_1.jpg"),
                        ExampleUtils.tmpFileNameOf("out/gif/frame_2.jpg"),
                        ExampleUtils.tmpFileNameOf("out/gif/frame_3.jpg"),
                        ExampleUtils.tmpFileNameOf("out/gif/frame_4.jpg"),
                        ExampleUtils.tmpFileNameOf("out/gif/frame_5.jpg"));
    }

    public static void splitImg() throws IOException {
        ImagePipes.of(ExampleUtils.tmpFileNameOf("in/before_split.jpg"))
                .addFilter(new GridSplitHandler.Builder()
                        .gridWidth(400)
                        .gridHeight(250)
                        .build())
                .toFiles(ExampleUtils.tmpFileNameOf("out/split/slice_1.jpg"),
                        ExampleUtils.tmpFileNameOf("out/split/slice_2.jpg"),
                        ExampleUtils.tmpFileNameOf("out/split/slice_3.jpg"),
                        ExampleUtils.tmpFileNameOf("out/split/slice_4.jpg"),
                        ExampleUtils.tmpFileNameOf("out/split/slice_5.jpg"),
                        ExampleUtils.tmpFileNameOf("out/split/slice_6.jpg"),
                        ExampleUtils.tmpFileNameOf("out/split/slice_7.jpg"),
                        ExampleUtils.tmpFileNameOf("out/split/slice_8.jpg"),
                        ExampleUtils.tmpFileNameOf("out/split/slice_9.jpg"));
    }

    public static void mergeImg() throws IOException {
        ImagePipes.of(ExampleUtils.tmpFileNameOf("in/to_merge/spring.jpg"),
                        ExampleUtils.tmpFileNameOf("in/to_merge/summer.jpg"),
                        ExampleUtils.tmpFileNameOf("in/to_merge/winter.jpg"),
                        ExampleUtils.tmpFileNameOf("in/to_merge/autumn.jpg"))
                .addFilter(new GridMergeHandler.Builder()
                        .horizontalNum(2)
                        .fillColor(ColorUtils.of(240, 240, 240))
                        .gridWidth(530)
                        .gridHeight(530)
                        .autoAdapts(true)
                        .alignCenter(true)
                        .build())
                .toFile(ExampleUtils.tmpFileNameOf("out/after_merge.jpg"));
    }

    public static void mergeToGif() throws IOException {
        ImagePipes.of(ExampleUtils.tmpFileNameOf("in/to_merge/spring.jpg"),
                        ExampleUtils.tmpFileNameOf("in/to_merge/summer.jpg"),
                        ExampleUtils.tmpFileNameOf("in/to_merge/winter.jpg"),
                        ExampleUtils.tmpFileNameOf("in/to_merge/autumn.jpg"))
                .toFile(new GifFileEncoder.Builder()
                        .filename(ExampleUtils.tmpFileNameOf("out/seasons.gif"))
                        .delay(400)
                        .repeat(0)
                        .build());
    }
}
