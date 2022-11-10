package cn.extension;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

/**
 * 包装器
 *
 * @author tracy
 * @since 1.0.0
 */
public interface ImageWrapper<T, Children> extends Serializable {

    /**
     * 获取首个图像
     * @return 图像
     * @throws IOException IOException
     */
    BufferedImage firstImage() throws IOException;

    /**
     * 清空包装对象
     * @return ImageWrapper
     */
    Children clear();

    /**
     * 获取包装对象
     * @return 包装对象
     */
    T getWrapper();
}
