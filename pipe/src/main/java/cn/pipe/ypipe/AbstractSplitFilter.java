package cn.pipe.ypipe;

import cn.core.PipeFilter;
import cn.core.ex.HandlingException;
import cn.core.utils.CollectionUtils;
import cn.core.utils.ObjectUtils;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * An abstract pipe filter which used to split one images into multiple slices.
 *
 * @author tracy
 * @since 0.2.1
 */
public abstract class AbstractSplitFilter implements PipeFilter {

    @Override
    public List<BufferedImage> execute(List<BufferedImage> images) {
        ObjectUtils.excNull(images, "Source images is null.");
        CollectionUtils.excEmpty(images, "Not any source image was found.");
        if (images.size() > 1) {
            throw new HandlingException("Splitting of multiple images is not supported.");
        }
        return split(images.get(0));
    }

    /**
     * Split one image into multiple images.
     *
     * @param image The source image.
     * @return The final images after split.
     */
    protected abstract List<BufferedImage> split(BufferedImage image);
}
