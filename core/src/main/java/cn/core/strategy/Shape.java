package cn.core.strategy;

import java.awt.*;

/**
 * A shape drawn on an image.
 *
 * @author tracy
 * @since 0.2.1
 */
public interface Shape {

    /**
     * Paint this shape on the image.
     *
     * @param canvasWidth The canvas width.
     * @param canvasHeight The canvas height.
     * @param g2d The Graphics2D object of original image.
     */
    void paint(int canvasWidth, int canvasHeight, Graphics2D g2d);
}
