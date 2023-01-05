package cn.t8s.filter;

import cn.core.strategy.Shape;
import cn.core.utils.BufferedImageUtils;
import net.coobird.thumbnailator.filters.ImageFilter;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An image filter that can overlay some shape.At the same time, it is also an adapter
 * that maintains a shape internally.
 *
 * @author tracy
 * @since 0.2.1
 */
public class ShapeAdaptor implements ImageFilter {

    /**
     * The shape which overlay to the image.
     */
    private final Shape shape;

    public ShapeAdaptor(Shape shape) {
        this.shape = shape;
    }

    @Override
    public BufferedImage apply(BufferedImage img) {
        // copy new image from original image
        BufferedImage tar = BufferedImageUtils.copy(img, img.getType());
        Graphics2D g2d = tar.createGraphics();
        // make the shape smoother
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // painting
        shape.paint(img.getWidth(), img.getHeight(), g2d);
        g2d.dispose();
        return tar;
    }
}
