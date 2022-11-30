package cn.ypipe;

import cn.core.YPipeFilter;
import cn.core.exec.HandlingException;
import cn.core.utils.CollectionUtils;
import cn.core.utils.ObjectUtils;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 拆分图像策略
 *
 * @author tracy
 * @since 1.0.0
 */
public abstract class AbstractSplitFilter implements YPipeFilter {

    @Override
    public List<BufferedImage> execute(List<BufferedImage> images) {
        ObjectUtils.excNull(images, "source images is null");
        CollectionUtils.excEmpty(images, "not any source image was found");
        if (images.size() > 1) {
            throw new HandlingException("cannot split multiple images");
        }
        return split(images.get(0));
    }

    /**
     * 合并图像
     * @param image 源
     * @return 目标
     */
    protected abstract List<BufferedImage> split(BufferedImage image);
}
