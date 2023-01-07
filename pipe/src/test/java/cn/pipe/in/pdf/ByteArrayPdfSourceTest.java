package cn.pipe.in.pdf;

import cn.pipe.TestUtils;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayPdfSourceTest {

    @Test
    public void test_doLoad() throws IOException {
        // given
        InputStream is = TestUtils.getResourceStream("input/sequence.pdf");
        byte[] bytes = IOUtils.toByteArray(is);

        // when
        ByteArrayPdfSource source = new ByteArrayPdfSource(bytes);
        PDDocument document = source.doLoad();

        // then
        Assert.assertNotNull(document);
        // The PDF file has only 3 pages.
        Assert.assertEquals(3, document.getNumberOfPages());
    }

}