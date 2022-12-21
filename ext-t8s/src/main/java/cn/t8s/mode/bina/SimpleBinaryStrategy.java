package cn.t8s.mode.bina;

import cn.core.ex.InvalidSettingException;
import cn.core.strategy.mode.AbstractBinaryStrategy;
import cn.t8s.mode.graying.AvgGrayingStrategy;
import cn.core.strategy.mode.AbstractGrayingStrategy;
import cn.core.GenericBuilder;
import cn.core.tool.Range;
import java.awt.image.BufferedImage;

/**
 * A simple binary strategy that requires setting a threshold value.
 * Any pixel whose gray value is greater than the threshold value
 * will be set as the {@link SimpleBinaryStrategy#BINARY_MAX},
 * otherwise it will be set as the {@link SimpleBinaryStrategy#BINARY_MIN}.
 *
 * @author tracy
 * @since 0.2.1
 */
public class SimpleBinaryStrategy extends AbstractBinaryStrategy {

    /**
     * The larger value of the binarization.
     */
    public static final int BINARY_MIN = 0;

    /**
     * The smaller value of the binarization.
     */
    public static final int BINARY_MAX = 255;

    /**
     * The threshold value.
     */
    protected final int threshold;

    public SimpleBinaryStrategy(Builder bu) {
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

    public static class Builder implements GenericBuilder<SimpleBinaryStrategy> {
        protected AbstractGrayingStrategy grayingStrategy;
        protected int threshold = -1;

        public Builder grayingStrategy(AbstractGrayingStrategy grayingStrategy) {
            this.grayingStrategy = grayingStrategy;
            return this;
        }
        public Builder threshold(int threshold) {
            this.threshold = threshold;
            if (Range.ofInt(0, 255).notWithin(threshold)) {
                throw new InvalidSettingException("The threshold out of bounds:[0, 255].");
            }
            return this;
        }

        @Override
        public SimpleBinaryStrategy build() {
            // the default threshold is 128
            threshold = threshold <= 0 ? 128 : threshold;
            // the default graying strategy is average
            grayingStrategy = grayingStrategy == null ? new AvgGrayingStrategy() : grayingStrategy;
            return new SimpleBinaryStrategy(this);
        }
    }
}
