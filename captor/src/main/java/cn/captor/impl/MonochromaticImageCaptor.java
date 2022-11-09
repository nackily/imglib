package cn.captor.impl;

import cn.core.context.Range;
import cn.core.exc.ParameterException;
import cn.core.utils.ColorUtils;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 单色图像构建器
 *
 * @author tracy
 * @since 1.0.0
 */
public class MonochromaticImageCaptor extends TransparentImageCaptor {

    private final Color color;                              // 颜色
    private final float alpha;                              // 透明度，0-不透明，1-全透明

    public MonochromaticImageCaptor(Builder b) {
        super(b);
        this.color = b.color;
        this.alpha = b.alpha;
    }

    @Override
    public BufferedImage obtain() {
        BufferedImage bi = super.obtain();
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
        private final Color color;
        private final float alpha;

        public Builder() {
            this(ColorUtils.random(), 0f);
        }
        public Builder(Color color) {
            this(color, 0f);
        }
        public Builder(float alpha) {
            this(ColorUtils.random(), alpha);
        }
        public Builder(Color color, float alpha) {
            this.alpha = alpha;
            this.color = color;
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
