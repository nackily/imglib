package cn.core.in;

import cn.core.Source;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Buffered image data source.
 *
 * @param <S> the type of buffered image source
 * @author tracy
 * @since 0.2.1
 */
public interface BufferedImageSource<S> extends Source<S> {

    /**
     * Read an image from the buffered image source.
     * @return The read BufferedImage.
     * @throws IOException If some I/O exceptions occurred.
     */
    BufferedImage read() throws IOException;

    /**
     * Gets the original format name.
     * @return The original format name.
     */
    String getOriginalFormatName();

}
