package cn.core.in;

import cn.core.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ThisBufferedImageSourceTest {

    @Test
    public void test_read_correctUsage() throws IOException {
        // given
        BufferedImage originalImage = TestUtils.getImageFromResource("input/nothing.jpg");

        // when
        ThisBufferedImageSource source = new ThisBufferedImageSource(originalImage);
        BufferedImage readImage = source.read();

        // then
        Assert.assertEquals(originalImage, source.getSource());
        Assert.assertEquals(originalImage, readImage);
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