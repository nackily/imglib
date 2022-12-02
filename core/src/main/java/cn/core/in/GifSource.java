package cn.core.in;

import cn.core.Source;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * GifSource
 *
 * @author tracy
 * @since 1.0.0
 */
public interface GifSource<S> extends Source<S> {

    /**
     * 获取帧的数量
     * @return 数量
     * @throws IOException IO异常
     */
    int size() throws IOException;

    /**
     * 获取单帧
     * @param frameIndex 帧索引
     * @return 图像
     * @throws IOException IO异常
     */
    BufferedImage read(int frameIndex) throws IOException;

    /**
     * 获取多帧
     * @param frameIndexes 帧索引数组
     * @return 多个图像
     * @throws IOException IO异常
     */
    List<BufferedImage> read(Integer[] frameIndexes) throws IOException;

    /**
     * 获取每一帧
     * @return 多个图像
     * @throws IOException IO异常
     */
    List<BufferedImage> readAll() throws IOException;
}
