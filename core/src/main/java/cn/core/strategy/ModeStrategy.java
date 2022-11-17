package cn.core.strategy;

import java.awt.image.BufferedImage;

/**
 * 模式策略
 *
 * @author tracy
 * @since 1.0.0
 */
public interface ModeStrategy {

    /**
     * 执行模式
     * @param img 图像
     */
    void execute(BufferedImage img);
}
