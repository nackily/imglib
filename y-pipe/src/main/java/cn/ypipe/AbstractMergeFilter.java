package cn.ypipe;

import cn.core.YPipeFilter;
import cn.core.utils.CollectionUtils;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

/**
 * 合并图像处理器
 *
 * @author tracy
 * @since 1.0.0
 */
public abstract class AbstractMergeFilter implements YPipeFilter {

    @Override
    public List<BufferedImage> execute(List<BufferedImage> images) {
        CollectionUtils.excNull(images, "source images is null");
        CollectionUtils.excEmpty(images, "not any source image was found");
        return Collections.singletonList(merge(images));
    }

    /**
     * 合并图像
     * @param images 源
     * @return 目标
     */
    protected abstract BufferedImage merge(List<BufferedImage> images);
}
