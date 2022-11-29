package cn.captor.source.pdf;

import cn.captor.source.PdfSource;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AbstractPdfBoxSource
 *
 * @author tracy
 * @since 1.0.0
 */
public abstract class AbstractPdfBoxSource<T> implements PdfSource<T> {

    protected final T source;
    protected boolean readCompleted = false;
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
     * 如果未加载 PDF，则进行加载
     * @throws IOException IO异常
     */
    protected abstract void loadIfNot() throws IOException;
}
