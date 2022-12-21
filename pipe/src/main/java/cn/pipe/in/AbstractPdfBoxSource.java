package cn.pipe.in;

import cn.core.in.PdfSource;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract superclass of pdf source.
 *
 * @param <T> The type of pdf source.
 * @author tracy
 * @since 0.2.1
 */
public abstract class AbstractPdfBoxSource<T> implements PdfSource<T> {

    /**
     * The source.
     */
    protected final T source;

    /**
     * The flag that indicating the completion of reading.
     */
    protected boolean readCompleted = false;

    /**
     * The pdf document object.
     */
    protected PDDocument pdf;


    protected AbstractPdfBoxSource(T source) {
        this.source = source;
    }

    @Override
    public int maxPageNumber() throws IOException {
        loadIfNot();
        return pdf.getNumberOfPages();
    }

    @Override
    public BufferedImage read(int page, float dpi) throws IOException {
        loadIfNot();
        PDFRenderer renderer = new PDFRenderer(pdf);
        return renderer.renderImageWithDPI(page, dpi, ImageType.RGB);
    }

    @Override
    public T getSource() {
        return source;
    }

    @Override
    public List<BufferedImage> read(Integer[] pages, float dpi) throws IOException {
        loadIfNot();
        PDFRenderer renderer = new PDFRenderer(pdf);

        List<BufferedImage> tars = new ArrayList<>();
        for (Integer page : pages) {
            tars.add(renderer.renderImageWithDPI(page, dpi, ImageType.RGB));
        }
        return tars;
    }

    @Override
    public boolean isReadCompleted() {
        return readCompleted;
    }

    /**
     * Load the pdf source if the source have not loaded.
     * @throws IOException If some I/O exceptions occurred when loading the pdf source.
     */
    protected abstract void loadIfNot() throws IOException;
}
