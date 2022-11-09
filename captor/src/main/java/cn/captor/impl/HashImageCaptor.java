package cn.captor.impl;

import cn.captor.ImageCaptor;
import cn.core.context.Range;
import cn.core.exc.ParameterException;
import cn.core.utils.ColorUtils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 散列图像构建器
 *
 * @author tracy
 * @since 1.0.0
 */
public class HashImageCaptor implements ImageCaptor {

    private final byte[] digest;                          // 摘要
    private final int gridVerticalNum;                    // 格子竖向的数量
    private final Color bgColor;                          // 背景色
    private final Color fgColor;                          // 前景色

    public HashImageCaptor(Builder b) {
        this.digest = b.digest;
        this.gridVerticalNum = b.gridVerticalNum;
        this.bgColor = b.bgColor == null ? ColorUtils.of(220, 220, 220) : b.bgColor;
        this.fgColor = b.fgColor == null ? ColorUtils.random() : b.fgColor;
    }

    @Override
    public BufferedImage obtain() {
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

    public static class Builder {
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
        public Builder(byte[] digest) {
            if (digest == null)
                throw new NullPointerException("empty digest");
            if (digest.length < 32) {
                throw new ParameterException("not a valid digest");
            }
            this.digest = digest;
        }
        public Builder gridVerticalNum(int gridVerticalNum) {
            if (Range.ofInt(1, 8).notWithin(gridVerticalNum))
                throw new ParameterException("vertical number of grid out of bound:[1, 8]");
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
        public HashImageCaptor build() {
            return new HashImageCaptor(this);
        }
    }
}
