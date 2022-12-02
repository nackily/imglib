package cn.t8s.filter;

import cn.core.ex.InvalidSettingException;
import cn.core.GenericBuilder;
import net.coobird.thumbnailator.filters.ImageFilter;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 高质量的图片扩张处理器
 *
 * @author tracy
 * @since 1.0.0
 */
public class HighQualityExpandHandler implements ImageFilter {

    /**
     * 保持宽高比，以宽和高中数值较大的一方为标尺
     */
    private final boolean keepAspectRatio;

    /**
     * 扩张至宽度等于指定值
     */
    private int finalWidth;

    /**
     * 扩张至高度等于指定值
     */
    private int finalHeight;

    public HighQualityExpandHandler(Builder bu) {
        this.keepAspectRatio = bu.keepAspectRatio;
        this.finalWidth = bu.finalWidth;
        this.finalHeight = bu.finalHeight;
    }

    @Override
    public BufferedImage apply(BufferedImage img) {
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
        // check setting validity
        float max = Math.max(radioW, radioH);
        if (max < 1) {
            throw new InvalidSettingException("width and height are less than the original value");
        }
        if (Math.min(radioW, radioH) < 1 && !keepAspectRatio) {
            throw new InvalidSettingException("width or height are less than the original value");
        }
        // reset the setting
        if (keepAspectRatio) {
            if (Float.valueOf(max).equals(radioW)) {
                finalHeight = (int) (max * originalHeight);
            } else {
                finalWidth = (int) (max * originalWidth);
            }
        }
    }

    public static class Builder implements GenericBuilder<HighQualityExpandHandler> {
        private boolean keepAspectRatio = true;
        private int finalWidth = -1;
        private int finalHeight = -1;

        public Builder keepAspectRatio(boolean keepAspectRatio) {
            this.keepAspectRatio = keepAspectRatio;
            return this;
        }

        public Builder finalWidth(int finalWidth) {
            this.finalWidth = finalWidth;
            if (finalWidth <= 0) {
                throw new InvalidSettingException("the final width must greater than 0 after expanded");
            }
            return this;
        }

        public Builder finalHeight(int finalHeight) {
            this.finalHeight = finalHeight;
            if (finalHeight <= 0) {
                throw new InvalidSettingException("the final height must greater than 0 after expanded");
            }
            return this;
        }

        @Override
        public HighQualityExpandHandler build() {
            boolean nonValidWidth = finalWidth <= 0;
            boolean nonValidHeight = finalHeight <= 0;
            if (nonValidWidth && nonValidHeight) {
                throw new InvalidSettingException("at least one dimension should be set");
            }
            boolean notValidGrid = nonValidWidth || nonValidHeight;
            if (!keepAspectRatio && notValidGrid) {
                throw new InvalidSettingException("both of dimensions should be set when expected to keep the aspect ratio");
            }
            return new HighQualityExpandHandler(this);
        }

    }
}
