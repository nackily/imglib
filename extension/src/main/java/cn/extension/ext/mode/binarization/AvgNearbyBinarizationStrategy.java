package cn.extension.ext.mode.binarization;

import cn.extension.ext.mode.AbstractGrayingStrategy;
import cn.extension.ext.mode.graying.AvgGrayingStrategy;
import java.awt.image.BufferedImage;

/**
 * 临近平均的二值化策略
 *
 * @author tracy
 * @since 1.0.0
 */
public class AvgNearbyBinarizationStrategy extends SimpleBinarizationStrategy {


    public AvgNearbyBinarizationStrategy(SimpleBinarizationStrategy.Builder bu) {
        super(bu);
    }

    @Override
    public void binaryImage (BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int leftTop = (x == 0 || y == 0) ? BINARY_MAX : img.getRGB(x - 1, y - 1) & 0xff;
                int leftCenter = x == 0 ? BINARY_MAX : img.getRGB(x - 1, y) & 0xff;
                int leftBottom = (x == 0 || y == h - 1) ? BINARY_MAX : img.getRGB(x - 1, y + 1) & 0xff;
                int currentTop = (y == 0) ? BINARY_MAX : img.getRGB(x, y - 1) & 0xff;
                int current = img.getRGB(x, y) & 0xff;
                int currentBottom = (y == h - 1) ? BINARY_MAX : img.getRGB(x, y + 1) & 0xff;
                int rightTop = (x == w - 1 || y == 0) ? BINARY_MAX : img.getRGB(x + 1, y - 1) & 0xff;
                int rightCenter = (x == w - 1) ? BINARY_MAX : img.getRGB(x + 1, y) & 0xff;
                int rightBottom = (x == w - 1 || y == h - 1) ? BINARY_MAX : img.getRGB(x + 1, y + 1) & 0xff;

                int avgGraynessValue = (leftTop + leftCenter + leftBottom + currentTop + current + currentBottom +
                        rightTop + rightCenter + rightBottom) / 9;

                int binaryValue = avgGraynessValue > threshold ? BINARY_MAX : BINARY_MIN;

                int binaryRgb = (binaryValue << 16) | (binaryValue << 8) | binaryValue;
                img.setRGB(x, y, binaryRgb);
            }
        }
    }

    public static class Builder extends SimpleBinarizationStrategy.Builder {

        @Override
        public Builder grayingStrategy(AbstractGrayingStrategy grayingStrategy) {
            super.grayingStrategy(grayingStrategy);
            return this;
        }

        @Override
        public Builder threshold(int threshold) {
            super.threshold(threshold);
            return this;
        }

        @Override
        public AvgNearbyBinarizationStrategy build() {
            // the default threshold is 128
            threshold = threshold <= 0 ? 128 : threshold;
            // the default graying strategy is average
            grayingStrategy = grayingStrategy == null ? new AvgGrayingStrategy() : grayingStrategy;
            return new AvgNearbyBinarizationStrategy(this);
        }
    }
}
