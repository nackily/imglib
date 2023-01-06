package cn.pipe.captor;

import cn.core.ex.InvalidSettingException;
import cn.core.tool.Range;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.ObjectUtils;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An image generator that can generate mono color image.
 *
 * @author tracy
 * @since 0.2.1
 */
public class MonoColorImageGenerator extends TransparentImageGenerator {

    /**
     * The color of the image.
     */
    private final Color color;

    /**
     * The alpha of the image which equals 0, indicating that the image is completely transparent.
     */
    private final float alpha;

    public MonoColorImageGenerator(Builder b) {
        super(b);
        this.color = b.color;
        this.alpha = b.alpha;
    }

    @Override
    public BufferedImage generate() {
        return BufferedImageUtils.newColoredImage(width, height, alpha, color);
    }

    public static class Builder extends TransparentImageGenerator.Builder {
        private Color color;
        private float alpha = 1f;

        @Override
        public Builder width(int width) {
            super.width = width;
            return this;
        }

        @Override
        public Builder height(int height) {
            super.height = height;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder alpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        @Override
        public MonoColorImageGenerator build() {
            super.build();
            ObjectUtils.excNull(color, "No color specified.");
            if (Range.ofFloat(0f, 1f).notWithin(alpha)) {
                throw new InvalidSettingException("Alpha out of bounds:[0, 1].");
            }

            return new MonoColorImageGenerator(this);
        }
    }
}
