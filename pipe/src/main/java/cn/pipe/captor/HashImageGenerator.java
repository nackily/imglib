package cn.pipe.captor;

import cn.core.ImageGenerator;
import cn.core.ex.InvalidSettingException;
import cn.core.GenericBuilder;
import cn.core.tool.Range;
import cn.core.utils.ColorUtils;
import cn.core.utils.ObjectUtils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * An image generator that can generate hash matrix image.
 *
 * @author tracy
 * @since 0.2.1
 */
public class HashImageGenerator implements ImageGenerator {

    /**
     * The digest of the hash image.
     */
    private final byte[] digest;

    /**
     * The vertical grid num of the hash image.
     */
    private final int gridVerticalNum;

    /**
     * The background color of the hash image.
     */
    private final Color bgColor;

    /**
     * The foreground color of the hash image.
     */
    private final Color fgColor;

    public HashImageGenerator(Builder b) {
        this.digest = b.digest;
        this.gridVerticalNum = b.gridVerticalNum;
        this.bgColor = b.bgColor;
        this.fgColor = b.fgColor;
    }

    @Override
    public BufferedImage generate() {
        int gridHorizontalNum = (gridVerticalNum + 1) >> 1;
        BufferedImage bi = new BufferedImage(gridVerticalNum, gridVerticalNum, BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < gridHorizontalNum; h++) {
            for (int v = 0; v < gridVerticalNum; v++) {
                int pos = gridVerticalNum * h + v;
                // paint foreground color for odd number,and paint background color for even number
                Color gridColor = (digest[pos] & 0x0001) == 1 ? fgColor : bgColor;
                // current grid,and symmetric grid which symmetrical by the mid-vertical-line
                bi.setRGB(h, v, gridColor.getRGB());
                bi.setRGB(gridVerticalNum - h - 1, v, gridColor.getRGB());
            }
        }
        return bi;
    }

    public static class Builder implements GenericBuilder<HashImageGenerator> {
        private final byte[] digest;
        private int gridVerticalNum = 8;
        private Color bgColor;
        private Color fgColor;

        public Builder(String content) throws NoSuchAlgorithmException {
            this(content, "SHA-256", 1);
        }
        public Builder(String content, String algorithm) throws NoSuchAlgorithmException {
            this(content, algorithm, 1);
        }
        public Builder(String content, String algorithm, int updateTimes) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            if (updateTimes > 0) {
                for (int i = 0; i < updateTimes; i++) {
                    md.update(content.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                md.update(content.getBytes(StandardCharsets.UTF_8));
            }
            digest = md.digest();
        }

        public Builder gridVerticalNum(int gridVerticalNum) {
            this.gridVerticalNum = gridVerticalNum;
            return this;
        }

        public Builder bgColor(Color bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public Builder fgColor(Color fgColor) {
            this.fgColor = fgColor;
            return this;
        }

        @Override
        public HashImageGenerator build() {
            ObjectUtils.excNull(digest, "Empty digest.");
            if (digest.length < 32) {
                throw new InvalidSettingException("Invalid digest setting.");
            }
            if (Range.ofInt(1, 8).notWithin(gridVerticalNum)) {
                throw new InvalidSettingException("The vertical number of grids is out of bounds:[1, 8].");
            }

            if (bgColor == null) {
                bgColor = ColorUtils.of(220, 220, 220);
            }
            if (fgColor == null) {
                fgColor = ColorUtils.random();
            }

            return new HashImageGenerator(this);
        }
    }
}
