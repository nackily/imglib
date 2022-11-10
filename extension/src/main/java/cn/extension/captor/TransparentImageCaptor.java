package cn.extension.captor;

import cn.extension.ImageCaptor;
import cn.extension.exec.ParameterException;
import cn.extension.tool.AbstractBuilder;

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
        this.width = b.width;
        this.height = b.height;
    }

    @Override
    public BufferedImage capture() {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        // make the background transparent
        bi = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        return bi;
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private int width;
        private int height;

        public TransparentImageCaptor build() {
            if (width <= 0 || height <= 0)
                throw new ParameterException(MessageFormat.format(
                        "size[{0}, {1}] out of bound", width, height));
            return new TransparentImageCaptor(this);
        }

        @Override
        public Builder set(String property, Object val) {
            if ("width".equals(property)) {
                width = (int) val;
            } else if ("height".equals(property)) {
                height = (int) val;
            } else {
                super.set(property, val);
            }
            return this;
        }
    }
}
