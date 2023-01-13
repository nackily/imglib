package cn.pipe.in;

import cn.pipe.BufferedImageComparer;
import cn.pipe.TestUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.Assert;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AbstractPdfBoxSourceTest {

    /**
     * A fixed template file of pdf source which loaded from '.../input/sequence.pdf'.
     * <br>
     * Just for unit testing.
     */
    private static class FixedTemplatePdfSource extends AbstractPdfBoxSource<Void> {
        protected FixedTemplatePdfSource() {
            super(null);
        }
        @Override
        protected PDDocument doLoad() throws IOException {
            InputStream is = TestUtils.getResourceStream("input/sequence.pdf");
            return PDDocument.load(is);
        }
    }


    @Test
    public void test_getSource() throws IOException {
        // given
        FixedTemplatePdfSource source = new FixedTemplatePdfSource();
        // when
        Void sr = source.getSource();
        // then
        Assert.assertNull(sr);

        // finally
        source.close();
    }

    @Test
    public void test_isReadCompleted() throws IOException {
        // given
        FixedTemplatePdfSource source = new FixedTemplatePdfSource();

        // when
        boolean before = source.isReadCompleted();
        source.loadIfNot();
        boolean after = source.isReadCompleted();

        // then
        Assert.assertFalse(before);
        Assert.assertTrue(after);

        // finally
        source.close();
    }

    @Test
    public void test_maxPageNumber() throws IOException {
        // given
        FixedTemplatePdfSource source = new FixedTemplatePdfSource();

        // when
        int pageSize = source.maxPageNumber();

        // then
        Assert.assertEquals(3, pageSize);

        // finally
        source.close();
    }

    @Test
    public void test_read() throws IOException {
        // given
        FixedTemplatePdfSource source = new FixedTemplatePdfSource();

        // when
        BufferedImage target = source.read(0, 300);

        // then
        PDDocument pdfDoc = PDDocument.load(TestUtils.getResourceStream("input/sequence.pdf"));
        BufferedImage image = new PDFRenderer(pdfDoc).renderImageWithDPI(0, 300, ImageType.RGB);

        Assert.assertTrue(BufferedImageComparer.isSame(image, target));

        // finally
        source.close();
        pdfDoc.close();
    }

    @Test
    public void test_read_withMultipart() throws IOException {
        // given
        FixedTemplatePdfSource source = new FixedTemplatePdfSource();

        // when
        List<BufferedImage> target = source.read(new Integer[]{0, 2}, 300);

        // then
        PDDocument pdfDoc = PDDocument.load(TestUtils.getResourceStream("input/sequence.pdf"));
        List<BufferedImage> pages = new ArrayList<>();
        PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);
        pages.add(pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB));
        pages.add(pdfRenderer.renderImageWithDPI(2, 300, ImageType.RGB));

        Assert.assertTrue(BufferedImageComparer.isSame(pages.get(0), target.get(0)));
        Assert.assertTrue(BufferedImageComparer.isSame(pages.get(1), target.get(1)));

        // finally
        source.close();
        pdfDoc.close();
    }

    @Test
    public void test_close() throws IOException {
        // given
        FixedTemplatePdfSource source = new FixedTemplatePdfSource();
        source.loadIfNot();

        // Before: unclose but read completed.
        Assert.assertFalse(source.isClosed());
        Assert.assertTrue(source.isReadCompleted());

        // when
        source.close();

        // After: closed.
        Assert.assertTrue(source.isClosed());
    }
}