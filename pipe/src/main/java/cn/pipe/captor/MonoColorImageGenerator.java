package cn.pipe.captor;

import cn.core.ex.InvalidSettingException;
import cn.core.tool.Range;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.ObjectUtils;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 单色图像构建器
 *
 * @author tracy
 * @since 1.0.0
 */
public class MonoColorImageGenerator extends TransparentImageGenerator {

    /**
     * 颜色
     */
    private final Color color;

    /**
     * 透明度，0-不透明，1-全透明
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
        private float alpha = 0f;

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
            ObjectUtils.excNull(color, "No color specified.");
            if (Range.ofFloat(0f, 1f).notWithin(alpha)) {
                throw new InvalidSettingException("Alpha out of bounds:[0, 1].");
            }

            return new MonoColorImageGenerator(this);
        }
    }
}
