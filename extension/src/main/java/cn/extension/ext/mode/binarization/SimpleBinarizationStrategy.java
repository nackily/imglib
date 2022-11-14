package cn.extension.ext.mode.binarization;

import cn.extension.exec.InvalidSettingException;
import cn.extension.ext.mode.AbstractBinarizationStrategy;
import cn.extension.ext.mode.AbstractGrayingStrategy;
import cn.extension.ext.mode.graying.AvgGrayingStrategy;
import cn.extension.tool.GenericBuilder;
import cn.extension.tool.Range;
import java.awt.image.BufferedImage;

/**
 * 简单的二值化策略
 *
 * @author tracy
 * @since 1.0.0
 */
public class SimpleBinarizationStrategy extends AbstractBinarizationStrategy {

    public static final int BINARY_MIN = 0;
    public static final int BINARY_MAX = 255;

    protected final int threshold;

    public SimpleBinarizationStrategy(Builder bu) {
        super(bu.grayingStrategy);
        this.threshold = bu.threshold;
    }

    @Override
    public void binaryImage (BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {

                int currentGraynessValue = img.getRGB(x, y) & 0xff;
                int binaryValue = currentGraynessValue > threshold ? BINARY_MAX : BINARY_MIN;

                int binaryRgb = (binaryValue << 16) | (binaryValue << 8) | binaryValue;
                img.setRGB(x, y, binaryRgb);
            }
        }
    }

    public static class Builder implements GenericBuilder<SimpleBinarizationStrategy> {
        protected AbstractGrayingStrategy grayingStrategy;
        protected int threshold = -1;

        public Builder grayingStrategy(AbstractGrayingStrategy grayingStrategy) {
            this.grayingStrategy = grayingStrategy;
            return this;
        }
        public Builder threshold(int threshold) {
            this.threshold = threshold;
            if (Range.ofInt(0, 255).notWithin(threshold)) {
                throw new InvalidSettingException("the threshold out of bound:[0, 255]");
            }
            return this;
        }

        @Override
        public SimpleBinarizationStrategy build() {
            // the default threshold is 128
            threshold = threshold <= 0 ? 128 : threshold;
            // the default graying strategy is average
            grayingStrategy = grayingStrategy == null ? new AvgGrayingStrategy() : grayingStrategy;
            return new SimpleBinarizationStrategy(this);
        }
    }
}
