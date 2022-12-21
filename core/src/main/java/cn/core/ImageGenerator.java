package cn.core;

import java.awt.image.BufferedImage;

/**
 * Image generator.
 *
 * @author tracy
 * @since 0.2.1
 */
public interface ImageGenerator {

    /**
     * Generate an image.
     *
     * @return The generated image.
     */
    BufferedImage generate();
}
