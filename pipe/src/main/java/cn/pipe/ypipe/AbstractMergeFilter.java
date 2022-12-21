package cn.pipe.ypipe;

import cn.core.PipeFilter;
import cn.core.utils.CollectionUtils;
import cn.core.utils.ObjectUtils;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

/**
 * An abstract pipe filter which used to merge multiple images into one image.
 *
 * @author tracy
 * @since 0.2.1
 */
public abstract class AbstractMergeFilter implements PipeFilter {

    @Override
    public List<BufferedImage> execute(List<BufferedImage> images) {
        ObjectUtils.excNull(images, "Source images is null.");
        CollectionUtils.excEmpty(images, "Not any source image was found.");
        return Collections.singletonList(merge(images));
    }

    /**
     * Merge multiple images into one image.
     *
     * @param images The source images.
     * @return The final image after merged.
     */
    protected abstract BufferedImage merge(List<BufferedImage> images);
}
