package cn.pipe.ypipe;

import cn.core.PipeFilter;
import cn.core.utils.CollectionUtils;
import cn.core.utils.ObjectUtils;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

/**
 * 合并图像处理器
 *
 * @author tracy
 * @since 1.0.0
 */
public abstract class AbstractMergeFilter implements PipeFilter {

    @Override
    public List<BufferedImage> execute(List<BufferedImage> images) {
        ObjectUtils.excNull(images, "Source images is null.");
        CollectionUtils.excEmpty(images, "Not any source image was found.");
        return Collections.singletonList(merge(images));
    }

    /**
     * 合并图像
     * @param images 源
     * @return 目标
     */
    protected abstract BufferedImage merge(List<BufferedImage> images);
}
