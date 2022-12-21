package cn.core;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * A pipe filter that can convert some images to another.It mainly
 * emphasizes the change of the number of images.For example,
 * combine multiple images into one or split one image into multiple
 * parts.
 *
 * @author tracy
 * @since 0.2.1
 */
public interface PipeFilter {

    /**
     * Execute this pipe filter for the original images.
     *
     * @param images The source images.
     * @return The final images.
     */
    List<BufferedImage> execute(List<BufferedImage> images);
}
