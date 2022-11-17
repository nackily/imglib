package cn.core;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Y-型管适配器
 *
 * @author tracy
 * @since 1.0.0
 */
public interface YPipeFilter {

    /**
     * 处理图像
     * @param images 图像源
     * @return 处理后图像
     */
    List<BufferedImage> execute(List<BufferedImage> images);
}
