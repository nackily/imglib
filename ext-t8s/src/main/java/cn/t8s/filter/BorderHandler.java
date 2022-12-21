package cn.t8s.filter;

import cn.core.GenericBuilder;
import cn.core.ex.InvalidSettingException;
import cn.core.tool.Range;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.ObjectUtils;
import net.coobird.thumbnailator.filters.ImageFilter;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An image filter that can add a border for image.
 *
 * @author tracy
 * @since 0.2.1
 */
public class BorderHandler implements ImageFilter {

    /**
     * The vertical margin of the border.
     */
    protected final int vMargins;

    /**
     * The horizontal margin of the border.
     */
    protected final int hMargins;

    /**
     * The alpha of the border which is limited to [0, 1]. When the alpha
     * is equal to 1.0, the border is completely transparent.
     */
    protected final float alpha;

    /**
     * The fill color of the border.
     */
    protected Color fillColor;

    public BorderHandler(Builder bu) {
        vMargins = bu.vMargins;
        hMargins = bu.hMargins;
        alpha = bu.alpha;
        if (bu.alpha == 1.0) {
            fillColor = null;
        } else {
            fillColor = bu.fillColor;
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
            if (hMargins <= 0) {
                throw new InvalidSettingException("The vertical margins of border must be greater than 0.");
            }
            return this;
        }

        public Builder hMargins(int hMargins) {
            this.hMargins = hMargins;
            if (hMargins <= 0) {
                throw new InvalidSettingException("The horizontal margins of border must be greater than 0.");
            }
            return this;
        }

        public Builder alpha(float alpha) {
            this.alpha = alpha;
            if (Range.ofFloat(0f, 1f).notWithin(alpha)) {
                throw new InvalidSettingException("Alpha out of bounds:[0, 1].");
            }
            return this;
        }

        public Builder fillColor(Color fillColor) {
            this.fillColor = fillColor;
            ObjectUtils.excNull(fillColor, "Fill color is null.");
            return this;
        }

        @Override
        public BorderHandler build() {
            return new BorderHandler(this);
        }
    }
}
