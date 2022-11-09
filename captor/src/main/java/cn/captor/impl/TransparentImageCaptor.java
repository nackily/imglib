package cn.captor.impl;

import cn.captor.ImageCaptor;
import cn.core.exc.ParameterException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

/**
 * 透明的图像构建器
 *
 * @author tracy
 * @since 1.0.0
 */
public class TransparentImageCaptor implements ImageCaptor {

    protected final int width;                                // 图像宽度
    protected final int height;                               // 图像高度


    public TransparentImageCaptor(Builder b) {
        if (b.width <= 0 || b.height <= 0)
            throw new ParameterException(MessageFormat.format(
                    "size[{0}, {1}] out of bound", b.width, b.height));
        this.width = b.width;
        this.height = b.height;
    }

    @Override
    public BufferedImage obtain() {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        // make the background transparent
        bi = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        return bi;
    }

    public static class Builder {
        private int width;
        private int height;

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }
        public TransparentImageCaptor build() {
            return new TransparentImageCaptor(this);
        }
    }
}
