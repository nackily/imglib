package cn.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * 图像编码器
 *
 * @author tracy
 * @since 1.0.0
 */
public interface BufferedImageEncoder {

    /**
     * 是否支持编码多个图像
     * @return true-是；false-否
     */
    boolean supportMultiple();

    /**
     * 编码
     * @param sources 图像
     * @throws IOException IO异常
     */
    void encode(List<BufferedImage> sources) throws IOException;
}
