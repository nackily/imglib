package cn.pipe.in.pdf;

import cn.pipe.TestUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamPdfSourceTest {

    @Test
    public void test_doLoad() throws IOException {
        // given
        InputStream is = TestUtils.getResourceStream("input/sequence.pdf");

        // when
        InputStreamPdfSource source = new InputStreamPdfSource(is);
        PDDocument document = source.doLoad();

        // then
        Assert.assertNotNull(document);
        // The PDF file has only 3 pages.
        Assert.assertEquals(3, document.getNumberOfPages());
    }

}