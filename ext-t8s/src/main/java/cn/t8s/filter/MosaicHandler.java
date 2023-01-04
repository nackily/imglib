package cn.t8s.filter;

import cn.core.ex.InvalidSettingException;
import cn.core.GenericBuilder;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.ColorUtils;
import cn.core.utils.ObjectUtils;
import net.coobird.thumbnailator.filters.ImageFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

/**
 * An image filter that can add a mosaic for image.
 *
 * @author tracy
 * @since 0.2.1
 */
public class MosaicHandler implements ImageFilter {

    /**
     * The side length of the mosaic block.
     */
    private final int sideLength;

    /**
     * The x coordinate of the upper left corner of the mosaic region.
     */
    private final int startX;

    /**
     * The y coordinate of the upper left corner of the mosaic region.
     */
    private final int startY;

    /**
     * The x coordinate of the down right corner of the mosaic region.
     */
    private int endX;

    /**
     * The y coordinate of the down right corner of the mosaic region.
     */
    private int endY;

    public MosaicHandler(Builder bu) {
        this.sideLength = bu.sideLength;
        this.startX = bu.startX;
        this.startY = bu.startY;
        this.endX = bu.startX + bu.width;
        this.endY = bu.startY + bu.height;
    }

    @Override
    public BufferedImage apply(BufferedImage img) {
        ObjectUtils.excNull(img, "Original image is null.");
        adjustRect(img.getWidth(), img.getHeight());
        // copy an image from original image
        BufferedImage tar = BufferedImageUtils.copy(img, img.getType());
        Graphics g = tar.getGraphics();

        int currentX = startX;
        while (currentX < endX) {
            int nextWidth = Math.min(sideLength, endX - currentX);
            int currentY = startY;
            while (currentY < endY) {
                int nextHeight = Math.min(sideLength, endY - currentY);
                // obtain RGB of color of the center
                int centerRgb = ColorUtils.obtainRectCenterRGB(img, currentX, currentY, nextWidth, nextHeight);
                g.setColor(ColorUtils.ofRGB(centerRgb));
                // painting rectangle
                g.fillRect(currentX, currentY, nextWidth, nextHeight);
                currentY += sideLength;
            }
            currentX += sideLength;
        }

        g.dispose();
        return tar;
    }

    private void adjustRect(int originalWidth, int originalHeight) {
        if (startX > originalWidth || startY > originalHeight) {
            throw new InvalidSettingException(MessageFormat.format(
                    "The starting point:[{0}, {1}] are not in image range:[{2}, {3}].",
                    startX, startY,
                    originalWidth, originalHeight));
        }

        endX = Math.min(originalWidth, endX);
        endY = Math.min(originalHeight, endY);
    }


    public static class Builder implements GenericBuilder<MosaicHandler> {

        private int sideLength;
        private int startX;
        private int startY;
        private Integer width;
        private Integer height;

        public Builder sideLength(int sideLength) {
            this.sideLength = sideLength;
            return this;
        }
        public Builder startX(int startX) {
            this.startX = startX;
            return this;
        }
        public Builder startY(int startY) {
            this.startY = startY;
            return this;
        }
        public Builder width(int width) {
            this.width = width;
            return this;
        }
        public Builder height(int height) {
            this.height = height;
            return this;
        }

        @Override
        public MosaicHandler build() {
            if (sideLength <= 0) {
                throw new InvalidSettingException("Side length must be greater than 0.");
            }
            if (startX < 0) {
                throw new InvalidSettingException("The start point's X must be greater than 0.");
            }
            if (startY < 0) {
                throw new InvalidSettingException("The start point's Y must be greater than 0.");
            }
            if (!ObjectUtils.isNull(width) && width <= 0) {
                throw new InvalidSettingException("The rectangle's width must be greater than 0.");
            }
            if (!ObjectUtils.isNull(height) && height <= 0) {
                throw new InvalidSettingException("The rectangle's height must be greater than 0.");
            }

            width = ObjectUtils.isNull(width) ? sideLength : width;
            height = ObjectUtils.isNull(height) ? sideLength : height;

            return new MosaicHandler(this);
        }
    }
}
