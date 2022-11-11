package cn.extension.captor;

import cn.extension.tool.Range;
import cn.extension.exec.ParameterException;
import cn.extension.utils.ColorUtils;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 单色图像构建器
 *
 * @author tracy
 * @since 1.0.0
 */
public class MonochromaticImageCaptor extends TransparentImageCaptor {

    /**
     * 颜色
     */
    private final Color color;

    /**
     * 透明度，0-不透明，1-全透明
     */
    private final float alpha;

    public MonochromaticImageCaptor(Builder b) {
        super(b);
        this.color = b.color == null ? ColorUtils.random() : b.color;
        this.alpha = b.alpha;
    }

    @Override
    public BufferedImage capture() {
        BufferedImage bi = super.capture();
        // need to add a foreground color
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(color);
        if (alpha > 0) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        g2d.fillRect(0, 0, super.width, super.height);
        g2d.dispose();
        return bi;
    }

    public static class Builder extends TransparentImageCaptor.Builder {
        private Color color;
        private float alpha = 0f;

        @Override
        public Builder setup(String property, Object val) {
            if ("color".equals(property)) {
                color = (Color) val;
            } else if ("alpha".equals(property)) {
                alpha = (float) val;
            } else if ("width".equals(property)) {
                width = (int) val;
            } else if ("height".equals(property)) {
                height = (int) val;
            } else {
                unknownProperty(property, val);
            }
            return this;
        }

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
        public MonochromaticImageCaptor build() {
            if (color == null) {
                throw new ParameterException("no color specified");
            }
            if (Range.ofFloat(0f, 1f).notWithin(alpha)) {
                throw new ParameterException("alpha out of bound:[0.0, 1.0]");
            }

            return new MonochromaticImageCaptor(this);
        }
    }
}
