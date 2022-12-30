package cn.core.in;

import cn.core.TestUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.io.IOException;

public class FileImageSourceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void test_read_correctUsage() throws IOException {
        // given
        File inputFile = TestUtils.copyResourceToTemporaryFile(
                "input/nothing.jpg",
                temporaryFolder);

        // when
        FileImageSource source = new FileImageSource(inputFile);
        source.read();

        // then
        Assert.assertEquals(inputFile, source.getSource());
        Assert.assertEquals("JPEG", source.getOriginalFormatName());
        Assert.assertTrue(source.isReadCompleted());
    }

    @Test
    public void testEx_constructor_nullFile() {
        NullPointerException exception = Assert.assertThrows(NullPointerException.class,
                () -> new FileImageSource((File) null));
        Assert.assertEquals("File is null.",
                exception.getMessage());
    }

    @Test
    public void testEx_constructor_emptyFilename() {
        NullPointerException exception = Assert.assertThrows(NullPointerException.class,
                () -> new FileImageSource(""));
        Assert.assertEquals("File name is null.",
                exception.getMessage());
    }
}