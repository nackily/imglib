package cn.t8s.filter;

import cn.core.GenericBuilder;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.ColorUtils;
import net.coobird.thumbnailator.filters.ImageFilter;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 边框
 *
 * @author tracy
 * @since 1.0.0
 */
public class BorderHandler implements ImageFilter {

    /**
     * 垂直方向边距
     */
    protected final int vMargins;

    /**
     * 水平方向边距
     */
    protected final int hMargins;

    /**
     * 边框透明度
     */
    protected final float alpha;

    /**
     * 边框填充颜色
     */
    protected Color fillColor;

    public BorderHandler(Builder bu) {
        this.vMargins = bu.vMargins <= 0 ? 20 : bu.vMargins;
        this.hMargins = bu.hMargins <= 0 ? 20 : bu.hMargins;
        this.alpha = bu.alpha;
        if (bu.alpha == 1.0) {
            this.fillColor = null;
        } else {
            this.fillColor = bu.fillColor;
            if (bu.fillColor == null) {
                this.fillColor = ColorUtils.random();
            }
        }
    }

    @Override
    public BufferedImage apply(BufferedImage img) {
        // create new image
        int width = img.getWidth() + (hMargins << 1);
        int height = img.getHeight() + (vMargins << 1);
        BufferedImage tar = BufferedImageUtils.newBackgroundImage(alpha, width, height, fillColor);
        // copy source image to new image
        Graphics2D g = tar.createGraphics();
        g.drawImage(img, hMargins, vMargins, null);
        g.dispose();
        return tar;
    }


    public static class Builder implements GenericBuilder<BorderHandler> {
        private int vMargins;
        private int hMargins;
        private float alpha = 0f;
        private Color fillColor;

        public Builder vMargins(int vMargins) {
            this.vMargins = vMargins;
            return this;
        }

        public Builder hMargins(int hMargins) {
            this.hMargins = hMargins;
            return this;
        }

        public Builder alpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        public Builder fillColor(Color fillColor) {
            this.fillColor = fillColor;
            return this;
        }

        @Override
        public BorderHandler build() {
            return new BorderHandler(this);
        }
    }
}
