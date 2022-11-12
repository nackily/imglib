package cn.example;

import cn.core.wrapper.DefaultWrapper;
import cn.extension.ext.split.GridSplitStrategy;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Application
 *
 * @author tracy
 * @since 1.0.0
 */
public class Application {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, AWTException {
        long l1 = System.currentTimeMillis();

//        DefaultWrapper
//                .from(new HashImageCaptor.Builder("my name")
//                        .gridVerticalNum(8)
//                        .fgColor(ColorUtils.of(200, 0, 200))
//                        .build())
//                .expand(new HighQualityExpandHandler.Builder()
//                        .keepAspectRatio(true)
//                        .finalHeight(240)
//                        .build())
//                .border(new BorderHandler.Builder()
//                        .vMargins(20)
//                        .fillColor(ColorUtils.of(200, 200, 0))
//                        .build())
//                .roundRect(new RoundRectHandler.Builder()
//                        .arcWidth(20)
//                        .build())
//                .getWrapper()
//                .scale(1.0)
//                .toFile("F:/4test/imglib/test-out/hash.png");


        Thumbnails.Builder<?> builder = DefaultWrapper
                .from("F:\\4test\\imglib\\test-out\\before_mosaic.jpeg")
                .getWrapper()
                .scale(1.0);

        List<BufferedImage> images = DefaultWrapper.from(builder)
                .split(new GridSplitStrategy.Builder()
                        .gridWidth(300)
                        .gridHeight(100)
                        .build())
                .getWrapper()
                .scale(1.0)
                .asBufferedImages();

        for (int i = 0; i < images.size(); i++) {
            ImageIO.write(images.get(i), "PNG", new File("F:\\4test\\imglib\\test-out\\test2\\" + i + ".png"));
        }
        System.out.println(System.currentTimeMillis() - l1);

    }

}
