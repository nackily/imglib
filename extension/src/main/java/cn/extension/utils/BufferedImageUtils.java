package cn.extension.utils;

import cn.extension.captor.MonochromaticImageCaptor;
import cn.extension.captor.TransparentImageCaptor;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * BufferedImageUtils
 *
 * @author tracy
 * @since 1.0.0
 */
public final class BufferedImageUtils {

    private BufferedImageUtils(){}

    public static BufferedImage newBackgroundImage(int width, int height) {
        return newBackgroundImage(0f, width, height, ColorUtils.random());
    }

    public static BufferedImage newBackgroundImage(int width, int height, Color fillColor) {
        return newBackgroundImage(0f, width, height, fillColor);
    }


    /**
     * 创建底图
     * @param alpha 透明度
     * @param width 宽度
     * @param height 高度
     * @param fillColor 填充颜色
     * @return 图像
     */
    public static BufferedImage newBackgroundImage(float alpha, int width, int height, Color fillColor) {
        BufferedImage tar;
        if (alpha == 1.0) {
            tar = new TransparentImageCaptor.Builder()
                    .setup("width", width)
                    .setup("height", height)
                    .build()
                    .capture();
        } else {
            tar = new MonochromaticImageCaptor.Builder()
                    .setup("width", width)
                    .setup("height", height)
                    .setup("color", fillColor)
                    .setup("alpha", alpha)
                    .build()
                    .capture();
        }
        return tar;
    }
}
