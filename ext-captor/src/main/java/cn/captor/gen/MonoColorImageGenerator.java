package cn.captor.gen;

import cn.core.exec.InvalidSettingException;
import cn.core.tool.Range;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.ColorUtils;
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
        this.color = b.color == null ? ColorUtils.random() : b.color;
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
            if (color == null) {
                throw new InvalidSettingException("no color specified");
            }
            if (Range.ofFloat(0f, 1f).notWithin(alpha)) {
                throw new InvalidSettingException("alpha out of bound:[0.0, 1.0]");
            }

            return new MonoColorImageGenerator(this);
        }
    }
}
