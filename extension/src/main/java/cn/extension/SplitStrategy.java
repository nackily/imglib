package cn.extension;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 拆分图像策略
 *
 * @author tracy
 * @since 1.0.0
 */
public interface SplitStrategy {

    /**
     * 拆分图像
     * @param img 图像
     * @return 拆分后的图像列表
     */
    List<BufferedImage> execute(BufferedImage img);
}
