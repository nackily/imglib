package cn.core.utils;

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
        if (alpha == 1.0) {
            return newTransparentImage(width, height);
        } else {
            return newColoredImage(width, height, alpha, fillColor);
        }
    }

    /**
     * 创建透明图像
     * @param width 宽度
     * @param height 高度
     * @return 图像
     */
    public static BufferedImage newTransparentImage(int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        // make the background transparent
        bi = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        return bi;
    }

    /**
     * 创建上色图像
     * @param width 宽度
     * @param height 高度
     * @return 图像
     */
    public static BufferedImage newColoredImage(int width, int height, float alpha, Color c) {
        BufferedImage image = newTransparentImage(width, height);
        // add a foreground color
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(c);
        if (alpha > 0) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return image;
    }


}
