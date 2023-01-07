package cn.usage.builder;

import cn.core.in.PdfSource;
import cn.usage.AbstractSourceBuilder;
import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.tool.Range;
import cn.core.utils.CollectionUtils;
import cn.core.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A source builder which form PDF source.
 *
 * @author tracy
 * @since 0.2.1
 */
public class PdfSourceBuilder<S> extends AbstractSourceBuilder<PdfSourceBuilder<S>> {

    private static final Log LOG = LogFactory.getLog(PdfSourceBuilder.class);

    /**
     * The PDF source.
     */
    protected final PdfSource<S> source;

    /**
     * Whether to include all pages.
     */
    private boolean containsAll = false;

    /**
     * The page indexes to be extracted.
     */
    private final Set<Integer> pages = new HashSet<>();

    /**
     * The DPI (dots per inch) to render at.
     */
    private float dpi;

    /**
     * Indicates whether the pdf source object is one-time. May need to
     * call the method {@link PdfSourceBuilder#release()} to release
     * resources when it is set to {@code false}.
     */
    private boolean disposable = true;

    public PdfSourceBuilder(PdfSource<S> pdfSource) {
        this.source = pdfSource;
    }

    public PdfSourceBuilder<S> unDisposable() {
        disposable = false;
        return this;
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

    /**
     * Release some resources and reset the status of source object.
     *
     * @throws IOException If some I/O exceptions occurred when closing resource.
     */
    public void release() throws IOException {
        source.close();
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
                    StringUtils.join(invalidPages, ",")));
        }

        List<BufferedImage> images = source.read(pages.toArray(new Integer[0]), val);

        // Release resources when the pdf source is not-time.
        if (disposable) {
            release();
        }

        return images;
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

    /**
     * Release some resources when developer forget.
     *
     * @throws Throwable If some I/O exceptions occurred when closing resource.
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            if (source != null) {
                LOG.warn( "Warning: You did not close a PDF Source." );
                release();
            }
        } finally {
            super.finalize();
        }
    }
}
