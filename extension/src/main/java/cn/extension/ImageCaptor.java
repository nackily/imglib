package cn.extension;

import java.awt.image.BufferedImage;

/**
 * 图像捕获器
 *
 * @author tracy
 * @since 1.0.0
 */
public interface ImageCaptor {

    /**
     * 构建图片
     * @return 图像
     */
    BufferedImage capture();
}
