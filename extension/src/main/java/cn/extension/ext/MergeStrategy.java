package cn.extension.ext;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 合并图像策略
 *
 * @author tracy
 * @since 1.0.0
 */
public interface MergeStrategy {

    /**
     * 合并图像
     * @param images 多个图像
     * @return 合并后的图像
     */
    BufferedImage execute(List<BufferedImage> images);

}
