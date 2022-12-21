package cn.core.strategy.mode;

import cn.core.strategy.ModeStrategy;
import java.awt.image.BufferedImage;

/**
 * Abstract strategy to binary the image.
 *
 * @author tracy
 * @since 0.2.1
 */
public abstract class AbstractBinaryStrategy implements ModeStrategy {

    protected AbstractGrayingStrategy grayingStrategy;

    protected AbstractBinaryStrategy(AbstractGrayingStrategy grayingStrategy) {
        this.grayingStrategy = grayingStrategy;
    }

    @Override
    public void execute(BufferedImage img) {
        // do gray first
        grayingStrategy.execute(img);
        // then do binary
        binaryImage(img);
    }

    /**
     * Convert the image to a binary image.This method will directly change the original image.
     * @param img The original image.
     */
    public abstract void binaryImage(BufferedImage img);
}
