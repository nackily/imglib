package cn.core;

import java.awt.image.BufferedImage;

/**
 * 图像捕获器
 *
 * @author tracy
 * @since 1.0.0
 */
public interface ImageGenerator {

    /**
     * 生成图像
     * @return 图像
     */
    BufferedImage generate();
}
