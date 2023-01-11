package cn.usage.builder;

import cn.core.ex.HandlingException;
import cn.core.in.BufferedImageSource;
import cn.core.in.InputStreamImageSource;
import cn.core.in.ThisBufferedImageSource;
import cn.usage.BufferedImageComparer;
import cn.usage.ReflectionUtils;
import cn.usage.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageSourceBuilderTest {

    Iterable<BufferedImageSource<InputStream>> sources;
    @Before
    public void init() throws IOException {
        sources = Arrays.asList(
                new InputStreamImageSource(TestUtils.getResourceStream("input/nothing.jpg")),
                new InputStreamImageSource(TestUtils.getResourceStream("input/frame/frame-0.jpg"))
        );
    }

    @Test
    public void test_useOriginalFormat() {
        // given
        ImageSourceBuilder<InputStream> builder = new ImageSourceBuilder<>(sources);

        // when
        boolean before = (boolean) ReflectionUtils.get("useOriginalFormat", builder);
        builder.useOriginalFormat();
        boolean after = (boolean) ReflectionUtils.get("useOriginalFormat", builder);

        // then
        Assert.assertFalse(before);
        Assert.assertTrue(after);
    }

    @Test
    public void test_obtainSourceImages() throws IOException {
        // given
        ImageSourceBuilder<InputStream> builder = new ImageSourceBuilder<>(sources)
                .useOriginalFormat();

        // when
        List<BufferedImage> target = builder.obtainSourceImages();

        // then
        Assert.assertEquals(2, target.size());
        Assert.assertEquals("JPEG",
                ReflectionUtils.getFromSuper("formatName", builder));

        // and then
        BufferedImage image0 = TestUtils.getImageFromResource("input/nothing.jpg");
        BufferedImage image1 = TestUtils.getImageFromResource("input/frame/frame-0.jpg");

        Assert.assertTrue(BufferedImageComparer.isSame(image0, target.get(0)));
        Assert.assertTrue(BufferedImageComparer.isSame(image1, target.get(1)));
    }

    @Test
    public void testEx_obtainSourceImages_multipleFormats() throws IOException {

        List<BufferedImageSource<InputStream>> list = Arrays.asList(
                new InputStreamImageSource(TestUtils.getResourceStream("input/nothing.jpg")),
                new InputStreamImageSource(TestUtils.getResourceStream("input/whiteboard.bmp"))
        );

        ImageSourceBuilder<InputStream> builder = new ImageSourceBuilder<>(list)
                .useOriginalFormat();
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                builder::obtainSourceImages);
        Assert.assertTrue(ex.getMessage()
                .startsWith("Multiple available original formats found:")
        );
    }

    @Test
    public void testEx_obtainSourceImages_noFormat() {

        // Instantiate some unformatted BufferedImage(s).
        List<BufferedImageSource<BufferedImage>> list = Arrays.asList(
                new ThisBufferedImageSource(new BufferedImage(10, 20, BufferedImage.TYPE_INT_RGB)),
                new ThisBufferedImageSource(new BufferedImage(20, 30, BufferedImage.TYPE_INT_RGB))
        );

        // Test for no original format.
        ImageSourceBuilder<BufferedImage> builder = new ImageSourceBuilder<>(list)
                .useOriginalFormat();
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                builder::obtainSourceImages);
        Assert.assertEquals("No available original format.",
                ex.getMessage());
    }
}