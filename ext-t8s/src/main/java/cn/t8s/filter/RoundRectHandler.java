package cn.t8s.filter;

import cn.core.ex.InvalidSettingException;
import cn.core.GenericBuilder;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.ObjectUtils;
import net.coobird.thumbnailator.filters.ImageFilter;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An image filter that can add a round rect for image.
 *
 * @author tracy
 * @since 0.2.1
 */
public class RoundRectHandler implements ImageFilter {

    /**
     * the horizontal diameter of the arc at the four corners
     */
    private final int arcWidth;

    /**
     * the vertical diameter of the arc at the four corners
     */
    private final int arcHeight;


    public RoundRectHandler(Builder bu) {
        this.arcWidth = bu.arcWidth;
        this.arcHeight = bu.arcHeight;
    }

    @Override
    public BufferedImage apply(BufferedImage img) {
        ObjectUtils.excNull(img, "Original image is null.");
        int w = img.getWidth();
        int h = img.getHeight();
        // new an t image
        BufferedImage tar = BufferedImageUtils.newTransparentImage(w, h);
        // paint round rectangle
        Graphics2D g2d = tar.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRoundRect(0, 0, w, h, arcWidth, arcHeight);
        g2d.setComposite(AlphaComposite.SrcIn);

        g2d.drawImage(img, 0, 0, w, h, null);

        g2d.dispose();
        return tar;
    }

    public static class Builder implements GenericBuilder<RoundRectHandler> {
        private int arcWidth;
        private int arcHeight;

        public Builder arcWidth(int arcWidth) {
            this.arcWidth = arcWidth;
            return this;
        }

        public Builder arcHeight(int arcHeight) {
            this.arcHeight = arcHeight;
            return this;
        }

        @Override
        public RoundRectHandler build() {
            if (arcWidth < 0) {
                throw new InvalidSettingException("The horizontal diameter of the arc must be greater than 0.");
            }
            if (arcHeight < 0) {
                throw new InvalidSettingException("The vertical diameter of the arc must be greater than 0.");
            }

            boolean invalidAw = arcWidth == 0;
            boolean invalidAh = arcHeight == 0;
            if (invalidAw && invalidAh) {
                throw new InvalidSettingException("The horizontal diameter and vertical diameter of the arc cannot both be less than or equal to 0.");
            }

            if (invalidAw || invalidAh) {
                arcWidth = arcHeight = Math.max(arcHeight, arcWidth);
            }

            return new RoundRectHandler(this);
        }
    }
}
