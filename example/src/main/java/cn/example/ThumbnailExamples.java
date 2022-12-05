package cn.example;

import cn.core.strategy.mode.AbstractGrayingStrategy;
import cn.core.utils.ColorUtils;
import cn.example.utils.ExampleUtils;
import cn.t8s.filter.*;
import cn.t8s.mode.bina.SimpleBinaryStrategy;
import cn.t8s.mode.graying.FixedGrayingStrategy;
import cn.t8s.mode.graying.WeightGrayingStrategy;
import cn.t8s.overlay.shape.closed.Oval;
import cn.t8s.overlay.shape.opened.Line;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.*;
import java.io.IOException;

/**
 * ThumbnailExamples
 *
 * @author tracy
 * @since 1.0.0
 */
public class ThumbnailExamples {
    private ThumbnailExamples(){}

    public static void addBorder() throws IOException {
        Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
                .addFilter(new BorderHandler.Builder()
                        .vMargins(30)
                        .hMargins(20)
                        .fillColor(ColorUtils.of(240, 230, 175))
                        .build())
                .scale(1.0)
                .toFile(ExampleUtils.tmpFileNameOf("out/bordered.jpg"));
    }

    public static void mosaic() throws IOException {
        Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
                .addFilter(new MosaicHandler.Builder()
                        .height(160)
                        .width(180)
                        .startX(480)
                        .startY(260)
                        .sideLength(10)
                        .build())
                .scale(1.0)
                .toFile(ExampleUtils.tmpFileNameOf("out/after_mosaic.jpg"));
    }

    public static void roundRect() throws IOException {
        Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
                .addFilter(new RoundRectHandler.Builder()
                        .arcWidth(100)
                        .arcHeight(100)
                        .build())
                .scale(1.0)
                .toFile(ExampleUtils.tmpFileNameOf("out/rounded.png"));
    }

    public static void expand() throws IOException {
        Thumbnails.of(ExampleUtils.tmpFileNameOf("in/matrix64.png"))
                .addFilter(new HighQualityExpandHandler.Builder()
                        .finalWidth(300)
                        .keepAspectRatio(true)
                        .build())
                .scale(1.0)
                .toFile(ExampleUtils.tmpFileNameOf("out/expanded.png"));
    }

    public static void graying() throws IOException {
        Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
                .addFilter(new ModeAdaptor(
                        new WeightGrayingStrategy.Builder()
                                .redWeight(0.3f)
                                .greenWeight(0.59f)
                                .build()))
                .scale(1.0)
                .toFile(ExampleUtils.tmpFileNameOf("out/grayed.jpg"));
    }

    public static void binary() throws IOException {
        AbstractGrayingStrategy strategy = new FixedGrayingStrategy(FixedGrayingStrategy.FixedOption.R);
        Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
                .addFilter(new ModeAdaptor(
                        new SimpleBinaryStrategy.Builder()
                                .grayingStrategy(strategy)
                                .threshold(120)
                                .build()))
                .scale(1.0)
                .toFile(ExampleUtils.tmpFileNameOf("out/binary.jpg"));
    }

    public static void overlayLine() throws IOException {
        Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
                .addFilter(new OverlayAdaptor(
                        new Line.Builder()
                                .start(new Point(200, 260))
                                .end(new Point(1000, 260))
                                .color(ColorUtils.random())
                                .stroke(new BasicStroke(6))
                                .build()))
                .scale(1.0)
                .toFile(ExampleUtils.tmpFileNameOf("out/overlaid_line.jpg"));
    }

    public static void overlayShape() throws IOException {
        Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
                .addFilter(new OverlayAdaptor(
                        new Oval.Builder()
                                .fill(true)
                                .color(ColorUtils.random())
                                .rect(new Rectangle(220, 110, 680, 350))
                                .build()))
                .scale(1.0)
                .toFile(ExampleUtils.tmpFileNameOf("out/overlaid_oval.jpg"));
    }

}
