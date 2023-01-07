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
    public BufferedImage read(int pageIndex, float dpi) throws IOException {
        loadIfNot();
        PDFRenderer renderer = new PDFRenderer(pdf);
        return renderer.renderImageWithDPI(pageIndex, dpi, ImageType.RGB);
    }

    @Override
    public T getSource() {
        return source;
    }

    @Override
    public List<BufferedImage> read(Integer[] pageIndexes, float dpi) throws IOException {
        loadIfNot();
        PDFRenderer renderer = new PDFRenderer(pdf);

        List<BufferedImage> tars = new ArrayList<>();
        for (Integer pIndex : pageIndexes) {
            tars.add(renderer.renderImageWithDPI(pIndex, dpi, ImageType.RGB));
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
    protected void loadIfNot() throws IOException {
        if (readCompleted) {
            return;
        }
        pdf = doLoad();
        readCompleted = true;
    }

    /**
     * Load the pdf source.
     * @return The object of loaded GIF.
     * @throws IOException If some I/O exceptions occurred when loading the pdf source.
     */
    protected abstract PDDocument doLoad() throws IOException;

    /**
     * Free resources and reset status.
     *
     * @throws IOException If some I/O exceptions occurred when loading the pdf source.
     */
    @Override
    public void close() throws IOException {
        if (pdf != null && !pdf.getDocument().isClosed()) {
            // release the object of PDDocument
            pdf.close();
            // reset the read completed flag
            readCompleted = false;
        }
    }
}
