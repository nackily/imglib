package cn.pipe.captor;

import cn.core.ImageGenerator;
import cn.core.ex.InvalidSettingException;
import cn.core.GenericBuilder;
import cn.core.utils.BufferedImageUtils;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

/**
 * An image generator that can generate transparent image.
 *
 * @author tracy
 * @since 0.2.1
 */
public class TransparentImageGenerator implements ImageGenerator {

    /**
     * The width of the image.
     */
    protected final int width;

    /**
     * The height of the image.
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
                        "Invalid size[{0}, {1}].", width, height));
            }
            return new TransparentImageGenerator(this);
        }
    }
}
