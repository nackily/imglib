package cn.extension.utils;

import cn.extension.Range;
import cn.extension.exec.ParameterException;
import java.awt.*;

/**
 * 颜色工具类
 *
 * @author tracy
 * @since 1.0.0
 */
public final class ColorUtils {

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
        if (rRange.getMin() < 0 || rRange.getMax() > 255)
            throw new ParameterException("color's R-range out of bound, must limited to [0, 255]");
        if (gRange.getMin() < 0 || gRange.getMax() > 255)
            throw new ParameterException("color's G-range out of bound, must limited to [0, 255]");
        if (bRange.getMin() < 0 || bRange.getMax() > 255)
            throw new ParameterException("color's B-range out of bound, must limited to [0, 255]");

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

}
