package cn.core;

import java.awt.image.BufferedImage;

/**
 * BufferedImageSource
 *
 * @author tracy
 * @since 1.0.0
 */
public interface BufferedImageSource<S> extends Source<S, BufferedImage> {

    /**
     * 获取原始格式名称
     * @return 原始格式名称
     */
    String getOriginalFormatName();
}
