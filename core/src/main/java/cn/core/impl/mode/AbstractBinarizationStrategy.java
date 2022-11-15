package cn.core.impl.mode;

import cn.core.ModeStrategy;
import java.awt.image.BufferedImage;

/**
 * 抽象的二值化策略
 *
 * @author tracy
 * @since 1.0.0
 */
public abstract class AbstractBinarizationStrategy implements ModeStrategy {

    protected AbstractGrayingStrategy grayingStrategy;

    public AbstractBinarizationStrategy(AbstractGrayingStrategy grayingStrategy) {
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
     * 二值化
     * @param img 原始图像
     */
    public abstract void binaryImage(BufferedImage img);
}
