package cn.core;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * BufferedImageSource
 *
 * @author tracy
 * @since 1.0.0
 */
public interface BufferedImageSource<S> extends Source<S> {

    /**
     * read
     * @return target object
     * @throws IOException IO异常
     */
    BufferedImage read() throws IOException;

    /**
     * 获取原始格式名称
     * @return 原始格式名称
     */
    String getOriginalFormatName();
}
