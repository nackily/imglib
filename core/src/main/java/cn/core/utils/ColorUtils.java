package cn.core.utils;

import cn.core.ex.InvalidSettingException;
import cn.core.tool.Range;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An util class for color.
 *
 * @author tracy
 * @since 0.2.1
 */
public final class ColorUtils {
    private ColorUtils(){}

    /**
     * Get a color.
     *
     * @param r The red component.
     * @param g The green component.
     * @param b The blue component.
     * @return The final color.
     */
    public static Color of(int r, int g, int b) {
        return new Color(r, g, b);
    }

    /**
     * Get a color.
     *
     * @param rgb The RGB value.
     * @return The final color.
     */
    public static Color ofRGB(int rgb) {
        return new Color(rgb);
    }

    /**
     * Get a random color.
     *
     * @return The final color.
     */
    public static Color random() {
        Range<Integer> range = Range.ofInt(0, 255);
        return random(range, range, range);
    }

    /**
     * Get a random color.
     *
     * @param rRange The range of red component.
     * @param gRange The range of green component.
     * @param bRange The range of blue component.
     * @return The final color.
     */
    public static Color random(Range<Integer> rRange, Range<Integer> gRange, Range<Integer> bRange) {
        if (rRange.getMin() < 0 || rRange.getMax() > 255) {
            throw new InvalidSettingException("Color's R-value out of bounds, must limited to [0, 255].");
        }
        if (gRange.getMin() < 0 || gRange.getMax() > 255) {
            throw new InvalidSettingException("Color's G-value out of bounds, must limited to [0, 255].");
        }
        if (bRange.getMin() < 0 || bRange.getMax() > 255) {
            throw new InvalidSettingException("Color's B-value out of bounds, must limited to [0, 255].");
        }

        int r = RandomUtils.randomInt(rRange.getMin(), rRange.getMax());
        int g = RandomUtils.randomInt(gRange.getMin(), gRange.getMax());
        int b = RandomUtils.randomInt(bRange.getMin(), bRange.getMax());
        return new Color(r, g, b);
    }

    /**
     * Select a color from multiple options.
     *
     * @param options All options.
     * @return The selected color.
     */
    public static Color anyOf(Color... options) {
        if (CollectionUtils.isNullOrEmpty(options)) {
            throw new InvalidSettingException("No options.");
        }
        int index = RandomUtils.randomInt(0, options.length - 1);
        return options[index];
    }

    /**
     * Gets the color's RGB of the center of the region.
     *
     * @param img The original image.
     * @param x The x coordinate of the upper left corner of the rectangle.
     * @param y The y coordinate of the upper left corner of the rectangle.
     * @param w The width of the rectangle.
     * @param h The height of the rectangle.
     * @return The color's RGB value.
     */
    public static int obtainRectCenterRGB(BufferedImage img, int x, int y, int w, int h) {
        ObjectUtils.excNull(img, "BufferedImage is null.");
        if(w <= 0) {
            throw new InvalidSettingException("The width of region cannot be less than or equal to 0.");
        }
        if(h <= 0) {
            throw new InvalidSettingException("The height of region cannot be less than or equal to 0.");
        }
        if(x < 0) {
            throw new InvalidSettingException("The x coordinate of the upper left corner cannot be less than or equal to 0.");
        }
        if(y < 0) {
            throw new InvalidSettingException("The y coordinate of the upper left corner cannot be less than or equal to 0.");
        }

        if((x + w) > img.getWidth()) {
            throw new InvalidSettingException("The specified region crosses the width of the image.");
        }
        if((y + h) > img.getHeight()) {
            throw new InvalidSettingException("The specified region crosses the height of the image.");
        }

        int centerX = x + w / 2;
        int centerY = y + h / 2;
        return img.getRGB(centerX, centerY);
    }

}
