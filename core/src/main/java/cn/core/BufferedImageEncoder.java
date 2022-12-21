package cn.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Encoder for buffered image.
 *
 * @author tracy
 * @since 0.2.1
 */
public interface BufferedImageEncoder {

    /**
     * Whether to support encoding multiple images.
     * @return true If this encoder supported multiple images.
     */
    boolean supportMultiple();

    /**
     * Encode images.
     * @param sources All source images to encode.
     * @throws IOException If exception occurred when encode source images.
     */
    void encode(List<BufferedImage> sources) throws IOException;
}
