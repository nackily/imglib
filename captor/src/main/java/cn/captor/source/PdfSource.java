package cn.captor.source;

import cn.core.Source;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * PdfSource
 *
 * @author tracy
 * @since 1.0.0
 */
public interface PdfSource<S> extends Source<S> {

    /**
     * 获取最大页码
     * @return 页码
     * @throws IOException IO异常
     */
    int maxPageNumber() throws IOException;

    /**
     * PDF 页转图像
     * @param page 页码
     * @param dpi DPI
     * @return 图像
     * @throws IOException IO异常
     */
    BufferedImage read(int page, float dpi) throws IOException;

    /**
     * PDF 页转图像列表
     * @param pages 页码数组
     * @param dpi DPI
     * @return 多个图像
     * @throws IOException IO异常
     */
    List<BufferedImage> read(Integer[] pages, float dpi) throws IOException;
}
