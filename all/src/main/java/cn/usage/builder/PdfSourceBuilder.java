package cn.usage.builder;

import cn.core.in.PdfSource;
import cn.usage.AbstractSourceBuilder;
import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.tool.Range;
import cn.core.utils.CollectionUtils;
import cn.core.utils.StringUtils;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * PdfSourceBuilder
 *
 * @author tracy
 * @since 0.2.1
 */
public class PdfSourceBuilder<S> extends AbstractSourceBuilder<PdfSourceBuilder<S>> {

    protected final PdfSource<S> source;
    private boolean containsAll = false;
    private final Set<Integer> pages = new HashSet<>();
    private float dpi;

    public PdfSourceBuilder(PdfSource<S> pdfSource) {
        this.source = pdfSource;
    }

    public PdfSourceBuilder<S> registerAll() {
        containsAll = true;
        return this;
    }

    public PdfSourceBuilder<S> register(int pageIndex) {
        checkPageIndex(pageIndex);
        pages.add(pageIndex);
        return this;
    }

    public PdfSourceBuilder<S> register(int... pageIndexes) {
        for (int index : pageIndexes) {
            checkPageIndex(index);
            pages.add(index);
        }
        return this;
    }

    public PdfSourceBuilder<S> register(Range<Integer> range) {
        checkPageIndex(range.getMin());
        for (int i = range.getMin(); i <= range.getMax(); i++) {
            pages.add(i);
        }
        return this;
    }

    public PdfSourceBuilder<S> dpi(float dpi) {
        if (dpi <= 0) {
            throw new InvalidSettingException("DPI must be greater than 0.");
        }
        this.dpi = dpi;
        return this;
    }

    @Override
    protected List<BufferedImage> obtainSourceImages() throws IOException {
        checkReadiness();

        // the max page index of the pdf
        int maxPageIndex = source.maxPageNumber() - 1;

        float val = dpi <= 0 ? 300 : dpi;

        // export all pages
        if (containsAll) {
            for (int index = 0; index < maxPageIndex; index++) {
                pages.add(index);
            }
        }

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

        return source.read(pages.toArray(new Integer[0]), val);
    }


    protected void checkReadiness() {
        if (CollectionUtils.isNullOrEmpty(pages)) {
            throw new HandlingException("No page indexes are registered.");
        }
    }


    private static void checkPageIndex(int pageIndex) {
        if (pageIndex < 0) {
            throw new InvalidSettingException("Page index must be greater than or equal to 0.");
        }
    }
}
