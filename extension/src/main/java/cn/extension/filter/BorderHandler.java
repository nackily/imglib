package cn.extension.filter;

import cn.extension.captor.MonochromaticImageCaptor;
import cn.extension.captor.TransparentImageCaptor;
import cn.extension.tool.AbstractBuilder;
import cn.extension.utils.ColorUtils;
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
        int width = img.getWidth() + (vMargins << 1);
        int height = img.getHeight() + (hMargins << 1);
        BufferedImage tar;
        if (alpha == 1.0) {
            tar = new TransparentImageCaptor.Builder()
                    .set("width", width)
                    .set("height", height)
                    .build()
                    .capture();
        } else {
            tar = new MonochromaticImageCaptor.Builder()
                    .set("width", width)
                    .set("height", height)
                    .set("color", fillColor)
                    .set("alpha", alpha)
                    .build()
                    .capture();
        }
        // copy source image to new image
        Graphics2D g = tar.createGraphics();
        g.drawImage(img, vMargins, hMargins, null);
        g.dispose();
        return tar;
    }


    public static class Builder extends AbstractBuilder<Builder, BorderHandler> {
        private int vMargins;
        private int hMargins;
        private float alpha = 0f;
        private Color fillColor;

        @Override
        public Builder set(String property, Object val) {
            if ("vMargins".equals(property)) {
                vMargins = (int) val;
            } else if ("hMargins".equals(property)) {
                hMargins = (int) val;
            } else if ("alpha".equals(property)) {
                alpha = (float) val;
            } else if ("fillColor".equals(property)) {
                fillColor = (Color) val;
            } else {
                super.set(property, val);
            }
            return this;
        }

        @Override
        public BorderHandler build() {
            return new BorderHandler(this);
        }
    }
}
