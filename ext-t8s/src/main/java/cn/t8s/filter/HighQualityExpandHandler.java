package cn.t8s.filter;

import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.GenericBuilder;
import cn.core.utils.ObjectUtils;
import net.coobird.thumbnailator.filters.ImageFilter;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An image filter that can enlarge an image lossless.
 *
 * @author tracy
 * @since 0.2.1
 */
public class HighQualityExpandHandler implements ImageFilter {

    /**
     * Set keep the aspect ratio. If true, only one of width and height should be set.
     */
    private final boolean keepAspectRatio;

    /**
     * The final width after expanded.
     */
    private int finalWidth;

    /**
     * The final height after expanded.
     */
    private int finalHeight;

    public HighQualityExpandHandler(Builder bu) {
        this.keepAspectRatio = bu.keepAspectRatio;
        this.finalWidth = bu.finalWidth;
        this.finalHeight = bu.finalHeight;
    }

    @Override
    public BufferedImage apply(BufferedImage img) {
        ObjectUtils.excNull(img, "Original image is null.");

        adjust(img.getWidth(), img.getHeight());
        // expand and write to new image
        Image tmp = img.getScaledInstance(finalWidth, finalHeight, Image.SCALE_FAST);
        BufferedImage tar = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null), img.getType());
        Graphics2D g2d = tar.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return tar;
    }

    private void adjust(int originalWidth, int originalHeight) {
        // calculate the scale of expand
        float radioW = (float) finalWidth / (float) originalWidth;
        float radioH = (float) finalHeight / (float) originalHeight;
        float maxRadio = Math.max(radioW, radioH);

        // reset the setting if necessary
        if (keepAspectRatio) {
            if (Float.valueOf(maxRadio).equals(radioW)) {
                finalHeight = (int) (maxRadio * originalHeight);
            } else {
                finalWidth = (int) (maxRadio * originalWidth);
            }
        }

        // check setting validity
        if (originalWidth > finalWidth) {
            throw new HandlingException(String.format("The final width(%s) is less than the original width(%s).",
                    finalWidth, originalWidth));
        }
        if (originalHeight > finalHeight) {
            throw new HandlingException(String.format("The final height(%s) is less than the original height(%s).",
                    finalHeight, originalHeight));
        }
    }

    public static class Builder implements GenericBuilder<HighQualityExpandHandler> {
        private boolean keepAspectRatio = true;
        private int finalWidth;
        private int finalHeight;

        public Builder keepAspectRatio(boolean keepAspectRatio) {
            this.keepAspectRatio = keepAspectRatio;
            return this;
        }

        public Builder finalWidth(int finalWidth) {
            this.finalWidth = finalWidth;
            return this;
        }

        public Builder finalHeight(int finalHeight) {
            this.finalHeight = finalHeight;
            return this;
        }

        @Override
        public HighQualityExpandHandler build() {
            if (finalWidth < 0) {
                throw new InvalidSettingException("The final width after expanded must greater than 0.");
            }
            if (finalHeight < 0) {
                throw new InvalidSettingException("The final height after expanded must greater than 0.");
            }

            boolean nonValidWidth = finalWidth <= 0;
            boolean nonValidHeight = finalHeight <= 0;

            if (keepAspectRatio) {
                if (nonValidWidth && nonValidHeight) {
                    throw new InvalidSettingException("At least one dimension should be set when expected not to keep the aspect ratio.");
                }
            } else {
                if (nonValidWidth || nonValidHeight) {
                    throw new InvalidSettingException("Both of dimensions should be set when expected not to keep the aspect ratio.");
                }
            }

            return new HighQualityExpandHandler(this);
        }

    }
}
