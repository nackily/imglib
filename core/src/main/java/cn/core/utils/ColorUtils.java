package cn.core.utils;

import cn.core.exec.InvalidSettingException;
import cn.core.tool.Range;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 颜色工具类
 *
 * @author tracy
 * @since 1.0.0
 */
public final class ColorUtils {
    private ColorUtils(){}

    /**
     * 颜色
     * @param r R分量
     * @param g G分量
     * @param b B分量
     * @return 颜色
     */
    public static Color of(int r, int g, int b) {
        return new Color(r, g, b);
    }

    /**
     * 颜色
     * @param rgb RGB
     * @return 颜色
     */
    public static Color ofRGB(int rgb) {
        return new Color(rgb);
    }

    /**
     * 随机色
     * @return 颜色
     */
    public static Color random() {
        Range<Integer> range = Range.ofInt(0, 255);
        return random(range, range, range);
    }

    /**
     * 随机色
     * @param rRange R分量-限定范围
     * @param gRange G分量-限定范围
     * @param bRange B分量-限定范围
     * @return 颜色
     */
    public static Color random(Range<Integer> rRange, Range<Integer> gRange, Range<Integer> bRange) {
        if (rRange.getMin() < 0 || rRange.getMax() > 255) {
            throw new InvalidSettingException("color's R-range out of bound, must limited to [0, 255]");
        }
        if (gRange.getMin() < 0 || gRange.getMax() > 255) {
            throw new InvalidSettingException("color's G-range out of bound, must limited to [0, 255]");
        }
        if (bRange.getMin() < 0 || bRange.getMax() > 255) {
            throw new InvalidSettingException("color's B-range out of bound, must limited to [0, 255]");
        }

        int r = RandomUtils.randomInt(rRange.getMin(), rRange.getMax());
        int g = RandomUtils.randomInt(gRange.getMin(), gRange.getMax());
        int b = RandomUtils.randomInt(bRange.getMin(), bRange.getMax());
        return new Color(r, g, b);
    }

    /**
     * 从多个选项中随机选择
     * @param options 所有选项
     * @return 选择的颜色
     */
    public static Color anyOf(Color... options) {
        int index = RandomUtils.randomInt(0, options.length);
        return options[index];
    }

    /**
     * 获取区域正中心的颜色
     * @param img 图像
     * @param x 区域左上角坐标-X
     * @param y 区域左上角坐标-Y
     * @param w 区域宽度
     * @param h 区域高度
     * @return 颜色
     */
    public static int obtainRectCenterRGB(BufferedImage img, int x, int y, int w, int h) {
        int centerX = x + w / 2;
        int centerY = y + h / 2;
        return img.getRGB(centerX, centerY);
    }

}
