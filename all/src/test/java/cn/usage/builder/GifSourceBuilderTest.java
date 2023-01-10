package cn.usage.builder;

import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.in.GifSource;
import cn.core.tool.Range;
import cn.pipe.in.gif.InputStreamGifSource;
import cn.usage.BufferedImageComparer;
import cn.usage.ReflectionUtils;
import cn.usage.TestUtils;
import com.madgag.gif.fmsware.GifDecoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GifSourceBuilderTest {

    GifSource<InputStream> source;
    @Before
    public void init() throws IOException {
        InputStream is = TestUtils.getResourceStream("input/seasons.gif");
        source = new InputStreamGifSource(is);
    }

    @Test
    public void test_registerAll() {
        // given
        GifSourceBuilder<InputStream> builder = new GifSourceBuilder<>(source);

        // when
        boolean before = (boolean) ReflectionUtils.get("containsAll", builder);
        builder.registerAll();
        boolean after = (boolean) ReflectionUtils.get("containsAll", builder);

        // then
        Assert.assertFalse(before);
        Assert.assertTrue(after);
    }

    @Test
    public void test_register_single() {
        // given
        GifSourceBuilder<InputStream> builder = new GifSourceBuilder<>(source);

        // when
        builder.register(3);

        // then
        Set<Integer> frames = (Set<Integer>) ReflectionUtils.get("frames", builder);

        Assert.assertEquals(1, frames.size());
        Assert.assertTrue(frames.contains(3));
    }

    @Test
    public void test_register_array() {
        // given
        GifSourceBuilder<InputStream> builder = new GifSourceBuilder<>(source);

        // when
        builder.register(2, 4, 1);

        // then
        Set<Integer> frames = (Set<Integer>) ReflectionUtils.get("frames", builder);

        Assert.assertEquals(3, frames.size());
        Assert.assertTrue(frames.contains(2));
        Assert.assertTrue(frames.contains(4));
        Assert.assertTrue(frames.contains(1));
    }

    @Test
    public void test_register_range() {
        // given
        GifSourceBuilder<InputStream> builder = new GifSourceBuilder<>(source);

        // when
        builder.register(Range.ofInt(2, 4));

        // then
        Set<Integer> frames = (Set<Integer>) ReflectionUtils.get("frames", builder);

        Assert.assertEquals(3, frames.size());
        Assert.assertTrue(frames.contains(2));
        Assert.assertTrue(frames.contains(3));
        Assert.assertTrue(frames.contains(4));
    }

    @Test
    public void test_register_withDuplicateElement() {
        // given
        GifSourceBuilder<InputStream> builder = new GifSourceBuilder<>(source);

        // when
        builder.register(2)
                .register(2, 5, 1, 2, 1)
                .register(Range.ofInt(1, 2));

        // then
        Set<Integer> frames = (Set<Integer>) ReflectionUtils.get("frames", builder);

        Assert.assertEquals(3, frames.size());
        Assert.assertTrue(frames.contains(2));
        Assert.assertTrue(frames.contains(5));
        Assert.assertTrue(frames.contains(1));
    }

    @Test
    public void testEx_register_range_null() {
        GifSourceBuilder<InputStream> builder = new GifSourceBuilder<>(source);
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> builder.register((Range<Integer>) null));
        Assert.assertEquals("Range is null.", ex.getMessage());
    }

    @Test
    public void testEx_register_invalid() {
        GifSourceBuilder<InputStream> builder = new GifSourceBuilder<>(source);
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> builder.register(-1));
        Assert.assertEquals("Frame index must be greater than or equal to 0.",
                ex.getMessage());
    }

    @Test
    public void test_obtainSourceImages_specified() throws IOException {
        // given
        GifSourceBuilder<InputStream> builder = new GifSourceBuilder<>(source);
        int[] frameIndexes = {0, 1, 3};

        // when
        List<BufferedImage> target = builder.register(frameIndexes)
                .obtainSourceImages();

        // then
        Assert.assertEquals(3, target.size());

        GifDecoder decoder = new GifDecoder();
        decoder.read(TestUtils.getResourceStream("input/seasons.gif"));
        BufferedImage frame0 = decoder.getFrame(0);
        BufferedImage frame1 = decoder.getFrame(1);
        BufferedImage frame3 = decoder.getFrame(3);

        Assert.assertTrue(BufferedImageComparer.isSame(frame0, target.get(0)));
        Assert.assertTrue(BufferedImageComparer.isSame(frame1, target.get(1)));
        Assert.assertTrue(BufferedImageComparer.isSame(frame3, target.get(2)));
    }

    @Test
    public void test_obtainSourceImages_all() throws IOException {
        // given
        GifSourceBuilder<InputStream> builder = new GifSourceBuilder<>(source);

        // when
        List<BufferedImage> target = builder.registerAll()
                .obtainSourceImages();

        // then
        Assert.assertEquals(4, target.size());

        GifDecoder decoder = new GifDecoder();
        decoder.read(TestUtils.getResourceStream("input/seasons.gif"));

        for (int i = 0; i < decoder.getFrameCount(); i++) {
            BufferedImage frame = decoder.getFrame(i);
            Assert.assertTrue(BufferedImageComparer.isSame(frame, target.get(i)));
        }
    }

    @Test
    public void testEx_obtainSourceImages_frameIndexOutOfBound() {
        GifSourceBuilder<InputStream> builder = new GifSourceBuilder<>(source)
                .register(0, 1, 3, 6, 5);
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                builder::obtainSourceImages);
        Assert.assertEquals("Frame indexes:[5,6] has exceeded the max frame index of the gif document.",
                ex.getMessage());
    }

    @Test
    public void testEx_obtainSourceImages_noFrameRegistered() {
        GifSourceBuilder<InputStream> builder = new GifSourceBuilder<>(source);
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                builder::obtainSourceImages);
        Assert.assertEquals("No frame indexes are registered.",
                ex.getMessage());
    }

}