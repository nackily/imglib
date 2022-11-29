package cn.usage.builder;

import cn.captor.source.PdfSource;
import cn.core.exec.HandlingException;
import cn.core.exec.InvalidSettingException;
import cn.core.tool.Range;
import cn.core.utils.CollectionUtils;
import cn.core.utils.StringUtils;
import cn.usage.Captors;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * PdfSourceBuilder
 *
 * @author tracy
 * @since 1.0.0
 */
public class PdfSourceBuilder<S> extends Captors.Builder<S, PdfSourceBuilder<S>> {

    protected final PdfSource<S> source;
    private final Set<Integer> pages = new HashSet<>();
    private float dpi;

    public PdfSourceBuilder(PdfSource<S> pdfSource) {
        this.source = pdfSource;
    }

    public PdfSourceBuilder<S> page(int pageIndex) {
        pages.add(pageIndex);
        return this;
    }

    public PdfSourceBuilder<S> pages(int... pageIndexes) {
        for (int index : pageIndexes) {
            pages.add(index);
        }
        return this;
    }

    public PdfSourceBuilder<S> pages(Range<Integer> range) {
        for (int i = range.getMin(); i <= range.getMax(); i++) {
            pages.add(i);
        }
        return this;
    }

    public PdfSourceBuilder<S> dpi(float dpi) {
        this.dpi = dpi;
        return this;
    }

    @Override
    public BufferedImage obtainBufferedImage() throws IOException {
        checkReadiness();
        checkSingleOutput();
        return obtainBufferedImages().get(0);
    }

    @Override
    public List<BufferedImage> obtainBufferedImages() throws IOException {
        checkReadiness();

        // the max page index of the pdf
        int maxPageIndex = source.maxPageNumber() - 1;

        // check all page was in bound
        Set<String> invalidPages = pages.stream()
                .filter(p -> maxPageIndex < p)
                .map(Objects::toString)
                .collect(Collectors.toSet());
        if (!CollectionUtils.isNullOrEmpty(invalidPages)) {
            throw new InvalidSettingException(MessageFormat.format(
                    "the page indexes:[{0}] has exceeded the max page number of the pdf document",
                    StringUtils.join(invalidPages)));
        }
        float DPI = dpi <= 0 ? 300 : dpi;
        return source.read(pages.toArray(new Integer[0]), DPI);
    }

    protected void checkReadiness() {
        if (CollectionUtils.isNullOrEmpty(pages)) {
            throw new HandlingException("no page to export");
        }
    }

    protected void checkSingleOutput() {
        if (CollectionUtils.isNullOrEmpty(pages)) {
            throw new HandlingException("no page to export");
        }
        if (pages.size() > 1) {
            throw new HandlingException("cannot create one image from multiple original pdf pages");
        }
    }
}
