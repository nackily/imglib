package cn.example;

import cn.core.wrapper.ThumbnailsImageWrapper;
import cn.extension.captor.HashImageCaptor;
import cn.extension.filter.BorderHandler;
import cn.extension.filter.HighQualityExpandHandler;
import cn.extension.utils.ColorUtils;

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
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        long l1 = System.currentTimeMillis();

        ThumbnailsImageWrapper
                .from(new HashImageCaptor
                        .Builder("my name is jack")
                        .set("fgColor", ColorUtils.of(155, 155, 155))
                        .build())
                .expand(new HighQualityExpandHandler.Builder()
                                .set("keepAspectRatio", false)
                                .set("finalWidth", 200)
                                .set("finalHeight", 9)
                                .build())
                .border(new BorderHandler.Builder()
                        .set("vMargins", 20)
                        .set("hMargins", 20)
                        .set("alpha", 0f)
                        .set("fillColor", ColorUtils.of(255,0,255))
                        .build()
                )
                .getWrapper()
                .scale(1.0)
                .toFile("F:/4test/imglib/test-out/hash.png");

        System.out.println(System.currentTimeMillis() - l1);
    }

}
