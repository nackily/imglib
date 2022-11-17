package cn.usage.builder;

import cn.captor.source.PdfSource;
import cn.usage.Captors;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * PdfSourceBuilder
 *
 * @author tracy
 * @since 1.0.0
 */
public class PdfSourceBuilder<S> extends Captors.Builder<S, PdfSourceBuilder<S>> {

    protected final PdfSource<S> source;
    private Set<Integer> pages = new HashSet<>();

    public PdfSourceBuilder(PdfSource<S> pdfSource) {
        this.source = pdfSource;
    }

    public PdfSourceBuilder<S> get(int pageIndex) {
        pages.add(pageIndex);
        return this;
    }

    @Override
    public BufferedImage obtainBufferedImage() throws IOException {
        return null;
    }

    @Override
    public List<BufferedImage> obtainBufferedImages() throws IOException {
        return null;
    }


    public static class InputStreamPdfSourceIterator {

    }
}
