package cn.pipe.in.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FilePdfSourceTest {

    @Test
    public void test_doLoad() throws IOException {
        // given
        String path = Objects.requireNonNull(
                ClassLoader.getSystemClassLoader().getResource("input/sequence.pdf")
        ).getPath();
        File file = new File(path);

        // when
        FilePdfSource source = new FilePdfSource(file);
        PDDocument document = source.doLoad();

        // then
        Assert.assertNotNull(document);
        // The PDF file has only 3 pages.
        Assert.assertEquals(3, document.getNumberOfPages());
    }

}