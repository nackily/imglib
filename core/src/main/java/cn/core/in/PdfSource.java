package cn.core.in;

import cn.core.Source;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Pdf source.
 *
 * @param <S> the type of pdf source
 * @author tracy
 * @since 0.2.1
 */
public interface PdfSource<S> extends Source<S> {

    /**
     * Get the maximum page number.
     *
     * @return The maximum page number.
     * @throws IOException If some I/O exceptions occurred when loading the pdf source.
     */
    int maxPageNumber() throws IOException;

    /**
     * Read an image which the page indicated by the index.
     *
     * @param page The page index.
     * @param dpi The DPI (dots per inch) to render at.
     * @return The image which the page indicated by the index.
     * @throws IOException If some I/O exceptions occurred when loading the pdf source.
     */
    BufferedImage read(int page, float dpi) throws IOException;

    /**
     * Read images which pages indicated by the indexes.
     *
     * @param pages The page indexes.
     * @param dpi The DPI (dots per inch) to render at.
     * @return The images which pages indicated by the indexes.
     * @throws IOException If some I/O exceptions occurred when loading the pdf source.
     */
    List<BufferedImage> read(Integer[] pages, float dpi) throws IOException;
}
