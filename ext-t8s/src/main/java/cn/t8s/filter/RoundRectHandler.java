package cn.t8s.filter;

import cn.core.ex.InvalidSettingException;
import cn.core.GenericBuilder;
import cn.core.utils.BufferedImageUtils;
import net.coobird.thumbnailator.filters.ImageFilter;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 圆角处理器
 *
 * @author tracy
 * @since 1.0.0
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
        private int arcWidth = -1;
        private int arcHeight = -1;

        public Builder arcWidth(int arcWidth) {
            this.arcWidth = arcWidth;
            if (arcWidth <= 0) {
                throw new InvalidSettingException("The horizontal diameter of the arc must be greater than 0.");
            }
            return this;
        }

        public Builder arcHeight(int arcHeight) {
            this.arcHeight = arcHeight;
            if (arcHeight <= 0) {
                throw new InvalidSettingException("The vertical diameter of the arc must be greater than 0.");
            }
            return this;
        }

        @Override
        public RoundRectHandler build() {
            if (arcWidth == -1 && arcHeight == -1) {
                throw new InvalidSettingException("Both of the horizontal diameter and vertical diameter not set.");
            }
            if (arcWidth == -1 || arcHeight == -1) {
                arcWidth = arcHeight = Math.max(arcHeight, arcWidth);
            }
            return new RoundRectHandler(this);
        }
    }
}
