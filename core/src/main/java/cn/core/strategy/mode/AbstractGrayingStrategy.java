package cn.core.strategy.mode;

import cn.core.strategy.ModeStrategy;
import cn.core.utils.ObjectUtils;
import java.awt.image.BufferedImage;

/**
 * Abstract strategy to graying the image.
 *
 * @author tracy
 * @since 0.2.1
 */
public abstract class AbstractGrayingStrategy implements ModeStrategy {

    @Override
    public void execute(BufferedImage img) {
        ObjectUtils.excNull(img, "Image is null.");
        int w = img.getWidth();
        int h = img.getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;

                int grayness = getGraynessValue(r, g, b);

                int grayRgb = (grayness << 16) | (grayness << 8) | grayness;
                img.setRGB(x, y, grayRgb);
            }
        }
    }


    /**
     * Calculate the grayness value.
     *
     * @param r The red component.
     * @param g The green component.
     * @param b The blue component.
     * @return The final grayness value.
     */
    public abstract int getGraynessValue(int r, int g, int b);

}
