package cn.example;

import cn.core.wrapper.DefaultWrapper;
import cn.extension.ext.merge.GridMergeStrategy;
import net.coobird.thumbnailator.Thumbnails;

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
                .from("F:\\4test\\imglib\\test-out\\test\\1.png",
                        "F:\\4test\\imglib\\test-out\\test\\2.png",
                        "F:\\4test\\imglib\\test-out\\test\\3.png",
                        "F:\\4test\\imglib\\test-out\\test\\4.png",
                        "F:\\4test\\imglib\\test-out\\test\\5.png",
                        "F:\\4test\\imglib\\test-out\\test\\6.png",
                        "F:\\4test\\imglib\\test-out\\test\\7.png")
                .getWrapper()
                .scale(1.0);

        DefaultWrapper
                .from(builder)
                .merge(new GridMergeStrategy.Builder()
                        .autoAdapts(true)
                        .alpha(1f)
                        .horizontalNum(3)
                        .alignCenter(true)
                        .build())
                .getWrapper()
                .scale(1f)
                .toFile("F:\\4test\\imglib\\test-out\\merge.png");

        System.out.println(System.currentTimeMillis() - l1);
    }

}
