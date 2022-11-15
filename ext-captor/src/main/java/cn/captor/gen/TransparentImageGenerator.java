package cn.captor.gen;

import cn.core.ImageGenerator;
import cn.core.exec.InvalidSettingException;
import cn.core.GenericBuilder;
import cn.core.utils.BufferedImageUtils;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

/**
 * 透明的图像构建器
 *
 * @author tracy
 * @since 1.0.0
 */
public class TransparentImageGenerator implements ImageGenerator {

    /**
     * 图像宽度
     */
    protected final int width;

    /**
     * 图像高度
     */
    protected final int height;

    public TransparentImageGenerator(Builder b) {
        this.width = b.width;
        this.height = b.height;
    }

    @Override
    public BufferedImage generate() {
        return BufferedImageUtils.newTransparentImage(width, height);
    }

    public static class Builder implements GenericBuilder<TransparentImageGenerator> {
        protected int width;
        protected int height;

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        @Override
        public TransparentImageGenerator build() {
            if (width <= 0 || height <= 0) {
                throw new InvalidSettingException(MessageFormat.format(
                        "size[{0}, {1}] out of bound", width, height));
            }
            return new TransparentImageGenerator(this);
        }
    }
}
