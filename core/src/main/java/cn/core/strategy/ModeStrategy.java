package cn.core.strategy;

import java.awt.image.BufferedImage;

/**
 * A strategy for applying mode to an image.
 *
 * @author tracy
 * @since 0.2.1
 */
public interface ModeStrategy {

    /**
     * Execute this mode.This method will directly change the original image.
     *
     * @param img The original image.
     */
    void execute(BufferedImage img);
}
