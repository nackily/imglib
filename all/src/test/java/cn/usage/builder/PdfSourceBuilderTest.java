package cn.usage.builder;

import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.in.PdfSource;
import cn.core.tool.Range;
import cn.pipe.in.pdf.InputStreamPdfSource;
import cn.usage.BufferedImageComparer;
import cn.usage.ReflectionUtils;
import cn.usage.TestUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class PdfSourceBuilderTest {

    PdfSource<InputStream> source;
    @Before
    public void init() throws IOException {
        source = new InputStreamPdfSource(TestUtils.getResourceStream("input/sequence.pdf"));
    }


    @Test
    public void test_unDisposable() throws IOException {
        // given
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source);

        // when
        boolean before = (boolean) ReflectionUtils.get("disposable", builder);
        builder.unDisposable();
        boolean after = (boolean) ReflectionUtils.get("disposable", builder);

        // then
        Assert.assertTrue(before);
        Assert.assertFalse(after);
    }

    @Test
    public void test_register_single() {
        // when
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source)
                .register(3);

        // then
        Set<Integer> pages = (Set<Integer>) ReflectionUtils.get("pages", builder);
        Assert.assertTrue(pages.contains(3));
    }

    @Test
    public void testEx_register_invalid() {
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source);
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> builder.register(-3));
        Assert.assertEquals("Page index must be greater than or equal to 0.",
                ex.getMessage());
    }

    @Test
    public void test_register_array() {
        // when
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source)
                .register(3, 5);

        // then
        Set<Integer> pages = (Set<Integer>) ReflectionUtils.get("pages", builder);
        Assert.assertTrue(pages.contains(3));
        Assert.assertTrue(pages.contains(5));
    }

    @Test
    public void test_register_range() {
        // when
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source)
                .register(Range.ofInt(3, 5));

        // then
        Set<Integer> pages = (Set<Integer>) ReflectionUtils.get("pages", builder);
        Assert.assertTrue(pages.contains(3));
        Assert.assertTrue(pages.contains(4));
        Assert.assertTrue(pages.contains(5));
    }

    @Test
    public void testEx_register_range_null() {
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source);
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> builder.register((Range<Integer>) null));
        Assert.assertEquals("Range is null.",
                ex.getMessage());
    }

    @Test
    public void test_register_withDuplicateElement() {
        // given
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source);

        // when
        builder.register(2)
                .register(2, 5, 1, 2, 1)
                .register(Range.ofInt(1, 2));

        // then
        Set<Integer> pages = (Set<Integer>) ReflectionUtils.get("pages", builder);

        Assert.assertEquals(3, pages.size());
        Assert.assertTrue(pages.contains(2));
        Assert.assertTrue(pages.contains(5));
        Assert.assertTrue(pages.contains(1));
    }

    @Test
    public void test_register_all() {
        // given
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source);

        // when
        boolean before = (boolean) ReflectionUtils.get("containsAll", builder);
        builder.registerAll();
        boolean after = (boolean) ReflectionUtils.get("containsAll", builder);

        // then
        Assert.assertFalse(before);
        Assert.assertTrue(after);
    }

    @Test
    public void test_dpi() {
        // when
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source)
                .dpi(300);

        // then
        Assert.assertEquals(300f, ReflectionUtils.get("dpi", builder));
    }

    @Test
    public void testEx_dpi_invalid() {
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source);
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> builder.dpi(-20));
        Assert.assertEquals("DPI must be greater than 0.",
                ex.getMessage());
    }

    @Test
    public void test_release() throws IOException {
        // given
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source)
                .registerAll()
                .unDisposable();

        // Need to load the document first, otherwise don't need to release resources.
        builder.unDisposable().obtainSourceImages();

        // when
        boolean before = source.isClosed();
        builder.release();
        boolean after = source.isClosed();

        // then
        Assert.assertFalse(before);
        Assert.assertTrue(after);
    }

    @Test
    public void test_obtainSourceImages_specified() throws IOException {
        // given
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source)
                .register(0, 2)
                .dpi(270);

        // when
        List<BufferedImage> target = builder.obtainSourceImages();

        // then
        Assert.assertEquals(2, target.size());

        // and then
        try (PDDocument doc = PDDocument.load(TestUtils.getResourceStream("input/sequence.pdf"))) {
            PDFRenderer renderer = new PDFRenderer(doc);
            BufferedImage image0 = renderer.renderImageWithDPI(0, 270, ImageType.RGB);
            BufferedImage image2 = renderer.renderImageWithDPI(2, 270, ImageType.RGB);

            Assert.assertTrue(BufferedImageComparer.isSame(image0, target.get(0)));
            Assert.assertTrue(BufferedImageComparer.isSame(image2, target.get(1)));
        }
    }

    @Test
    public void test_obtainSourceImages_all() throws IOException {
        // given
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source)
                .registerAll()
                .dpi(270);

        // when
        List<BufferedImage> target = builder.obtainSourceImages();

        // then
        Assert.assertEquals(3, target.size());

        // and then
        try (PDDocument doc = PDDocument.load(TestUtils.getResourceStream("input/sequence.pdf"))) {
            PDFRenderer renderer = new PDFRenderer(doc);
            BufferedImage image0 = renderer.renderImageWithDPI(0, 270, ImageType.RGB);
            BufferedImage image1 = renderer.renderImageWithDPI(1, 270, ImageType.RGB);
            BufferedImage image2 = renderer.renderImageWithDPI(2, 270, ImageType.RGB);

            Assert.assertTrue(BufferedImageComparer.isSame(image0, target.get(0)));
            Assert.assertTrue(BufferedImageComparer.isSame(image1, target.get(1)));
            Assert.assertTrue(BufferedImageComparer.isSame(image2, target.get(2)));
        }
    }

    @Test
    public void testEx_obtainSourceImages_noPageRegistered() {
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source)
                .dpi(270);
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                builder::obtainSourceImages);
        Assert.assertEquals("No page indexes are registered.",
                ex.getMessage());
    }

    @Test
    public void testEx_obtainSourceImages_frameIndexOutOfBound() {
        // This pdf document has just only 3 pages.
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source)
                .register(0, 1, 3, 5)
                .dpi(270);
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                builder::obtainSourceImages);
        Assert.assertEquals("The page indexes:[3,5] has exceeded the max page number of the pdf document.",
                ex.getMessage());
    }

    @Test
    public void testEx_obtainSourceImages_withUnDisposable() throws IOException {
        // given
        PdfSourceBuilder<InputStream> builder = new PdfSourceBuilder<>(source)
                .register(0)
                .unDisposable()
                .dpi(270);

        // when
        List<BufferedImage> target = builder.obtainSourceImages();

        boolean before = source.isClosed();
        builder.release();
        boolean after = source.isClosed();

        // then
        Assert.assertEquals(1, target.size());

        Assert.assertFalse(before);
        Assert.assertTrue(after);

        // and then
        try (PDDocument doc = PDDocument.load(TestUtils.getResourceStream("input/sequence.pdf"))) {
            PDFRenderer renderer = new PDFRenderer(doc);
            BufferedImage image0 = renderer.renderImageWithDPI(0, 270, ImageType.RGB);

            Assert.assertTrue(BufferedImageComparer.isSame(image0, target.get(0)));
        }
    }
}