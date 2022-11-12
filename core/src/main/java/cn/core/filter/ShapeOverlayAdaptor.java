package cn.core.filter;

import cn.extension.ext.OverlayShape;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.util.BufferedImages;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 形状处理器适配器
 *
 * @author tracy
 * @since 1.0.0
 */
public class ShapeOverlayAdaptor implements ImageFilter {

    private final OverlayShape shape;

    public ShapeOverlayAdaptor(OverlayShape shape) {
        this.shape = shape;
    }

    @Override
    public BufferedImage apply(BufferedImage img) {
        // copy new image from original image
        BufferedImage tar = BufferedImages.copy(img);
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
