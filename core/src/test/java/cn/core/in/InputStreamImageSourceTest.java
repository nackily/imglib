package cn.core.in;

import cn.core.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamImageSourceTest {

    @Test
    public void test_read_correctUsage() throws IOException {
        // given
        InputStream inputStream = TestUtils.getResourceStream("input/nothing.jpg");

        // when
        InputStreamImageSource source = new InputStreamImageSource(inputStream);
        source.read();

        // then
        Assert.assertEquals(inputStream, source.getSource());
        Assert.assertTrue(source.isReadCompleted());
    }

    @Test
    public void testEx_constructor_nullBufferedImage() {
        NullPointerException exception = Assert.assertThrows(NullPointerException.class,
                () -> new ThisBufferedImageSource(null));
        Assert.assertEquals("BufferedImage is null.",
                exception.getMessage());
    }

}