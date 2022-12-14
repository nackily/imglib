package cn.t8s.filter;

import cn.core.ex.InvalidSettingException;
import cn.core.GenericBuilder;
import cn.core.utils.ColorUtils;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.util.BufferedImages;
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
        adjustRect(img.getWidth(), img.getHeight());
        // copy an image from original image
        BufferedImage tar = BufferedImages.copy(img, img.getType());

        Graphics2D g2d = tar.createGraphics();

        int currentX = startX;
        int rectWidth = Math.min(sideLength, endX - startX);
        while (currentX + rectWidth < endX) {

            int currentY = startY;
            int rectHeight = Math.min(sideLength, endY - startY);

            while (currentY + rectHeight < endY) {
                // obtain RGB of color of the center
                int rgb = ColorUtils.obtainRectCenterRGB(img, currentX, currentY, rectWidth, rectHeight);
                g2d.setColor(ColorUtils.ofRGB(rgb));
                // painting rectangle
                g2d.fillRect(currentX, currentY, rectWidth, rectHeight);

                currentY += rectHeight;
                rectHeight = Math.min(sideLength, endY - currentY);
            }
            currentX += rectWidth;
            rectWidth = Math.min(sideLength, endX - currentX);
        }

        g2d.dispose();
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

        private int sideLength = -1;
        private int startX;
        private int startY;
        private int width = -1;
        private int height = -1;

        public Builder sideLength(int sideLength) {
            this.sideLength = sideLength;
            if (sideLength <= 0) {
                throw new InvalidSettingException("Side length must be greater than 0.");
            }
            return this;
        }
        public Builder startX(int startX) {
            this.startX = startX;
            if (startX <= 0) {
                throw new InvalidSettingException("The start point's X must be greater than 0.");
            }
            return this;
        }
        public Builder startY(int startY) {
            this.startY = startY;
            if (startY <= 0) {
                throw new InvalidSettingException("The start point's Y must be greater than 0.");
            }
            return this;
        }
        public Builder width(int width) {
            this.width = width;
            if (width <= 0) {
                throw new InvalidSettingException("The rectangle's width must be greater than 0.");
            }
            return this;
        }
        public Builder height(int height) {
            this.height = height;
            if (height <= 0) {
                throw new InvalidSettingException("The rectangle's height must be greater than 0.");
            }
            return this;
        }

        @Override
        public MosaicHandler build() {
            if (sideLength == -1) {
                throw new InvalidSettingException("Side length not set.");
            }
            width = width == -1 ? sideLength : width;
            height = height == -1 ? sideLength : height;
            return new MosaicHandler(this);
        }
    }
}
