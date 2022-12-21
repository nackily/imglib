package cn.core.in;

import cn.core.Source;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Gif source.
 *
 * @param <S> the type of gif source
 * @author tracy
 * @since 0.2.1
 */
public interface GifSource<S> extends Source<S> {

    /**
     * Get the size of source frames.
     *
     * @return The size of frames.
     * @throws IOException If some I/O exceptions occurred when loading the gif source.
     */
    int size() throws IOException;

    /**
     * Get a single frame image.
     *
     * @param frameIndex The frame index.
     * @return The frame image.
     * @throws IOException If some I/O exceptions occurred when loading the gif source.
     */
    BufferedImage read(int frameIndex) throws IOException;

    /**
     * Get multiple frame images.
     *
     * @param frameIndexes The frame indexes.
     * @return The frame images.
     * @throws IOException If some I/O exceptions occurred when loading the gif source.
     */
    List<BufferedImage> read(Integer[] frameIndexes) throws IOException;

    /**
     * Get all frame images of this gif source.
     *
     * @return All frame images.
     * @throws IOException If some I/O exceptions occurred when loading the gif source.
     */
    List<BufferedImage> readAll() throws IOException;
}
