package cn.pipe.in;

import cn.core.ex.HandlingException;
import cn.pipe.BufferedImageComparer;
import cn.pipe.TestUtils;
import com.madgag.gif.fmsware.GifDecoder;
import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AbstractGifSourceTest {

    /**
     * A fixed template file of gif source which loaded from '.../input/test.gif'.
     * <br>
     * Just for unit testing.
     */
    private static class FixedTemplateGifSource extends AbstractGifSource<Void> {
        /**
         * The configuration of loading.
         *
         * <li>Set to 0: loading the template file and return the actual status
         * when executing the method of {@link FixedTemplateGifSource#doLoad()}.</li>
         *
         * <li>Set to 1: return 1 when executing the method of
         * {@link FixedTemplateGifSource#doLoad()}.</li>
         *
         * <li>Set to 2: return 2 when executing the method of
         * {@link FixedTemplateGifSource#doLoad()}.</li>
         *
         * <li>Set to other: return itself when executing the method of
         * {@link FixedTemplateGifSource#doLoad()}.</li>
         */
        private final int joint;
        protected FixedTemplateGifSource(int joint) {
            super(null);
            this.joint = joint;
        }
        protected FixedTemplateGifSource() {
            this(GifDecoder.STATUS_OK);
        }
        @Override
        protected int doLoad() throws IOException {
            if (joint == GifDecoder.STATUS_OK) {
                InputStream is = TestUtils.getResourceStream("input/test.gif");
                int status = decoder.read(is);
                readCompleted = true;
                return status;
            } else {
                return joint;
            }
        }
    }

    @Test
    public void test_getSource() {
        // given
        FixedTemplateGifSource source = new FixedTemplateGifSource();
        // when
        Void sr = source.getSource();
        // then
        Assert.assertNull(sr);
    }

    @Test
    public void test_isReadCompleted() throws IOException {
        // given
        FixedTemplateGifSource source = new FixedTemplateGifSource();

        // when
        boolean before = source.isReadCompleted();
        source.loadIfNot();
        boolean after = source.isReadCompleted();

        // then
        Assert.assertFalse(before);
        Assert.assertTrue(after);
    }

    @Test
    public void test_size() throws IOException {
        // given
        FixedTemplateGifSource source = new FixedTemplateGifSource();

        // when
        int size = source.size();

        // then
        Assert.assertEquals(4, size);
    }

    @Test
    public void test_read() throws IOException {
        // given
        FixedTemplateGifSource source = new FixedTemplateGifSource();

        // when
        BufferedImage target = source.read(0);

        // then
        GifDecoder decoder = new GifDecoder();
        decoder.read(TestUtils.getResourceStream("input/test.gif"));
        BufferedImage frame0 = decoder.getFrame(0);

        Assert.assertTrue(BufferedImageComparer.isSame(frame0, target));
    }

    @Test
    public void test_read_withMultipart() throws IOException {
        // given
        FixedTemplateGifSource source = new FixedTemplateGifSource();

        // when
        List<BufferedImage> target = source.read(new Integer[]{0, 2});

        // then
        GifDecoder decoder = new GifDecoder();
        decoder.read(TestUtils.getResourceStream("input/test.gif"));
        List<BufferedImage> frames = new ArrayList<>();
        frames.add(decoder.getFrame(0));
        frames.add(decoder.getFrame(2));

        Assert.assertTrue(BufferedImageComparer.isSame(frames.get(0), target.get(0)));
        Assert.assertTrue(BufferedImageComparer.isSame(frames.get(1), target.get(1)));
    }

    @Test
    public void test_readAll() throws IOException {
        // given
        FixedTemplateGifSource source = new FixedTemplateGifSource();

        // when
        List<BufferedImage> target = source.readAll();

        // then
        GifDecoder decoder = new GifDecoder();
        decoder.read(TestUtils.getResourceStream("input/test.gif"));
        List<BufferedImage> frames = new ArrayList<>();
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            frames.add(decoder.getFrame(i));
        }

        for (int i = 0; i < frames.size(); i++) {
            Assert.assertTrue(BufferedImageComparer.isSame(frames.get(i), target.get(i)));
        }
    }

    @Test
    public void testEx_loadIfNot_statusError() {
        // decode error
        FixedTemplateGifSource source1 = new FixedTemplateGifSource(GifDecoder.STATUS_FORMAT_ERROR);
        HandlingException ex1 = Assert.assertThrows(HandlingException.class,
                source1::loadIfNot);
        Assert.assertEquals("Error decoding file (may be partially decoded).",
                ex1.getMessage());

        // open source error
        FixedTemplateGifSource source2 = new FixedTemplateGifSource(GifDecoder.STATUS_OPEN_ERROR);
        HandlingException ex2 = Assert.assertThrows(HandlingException.class,
                source2::loadIfNot);
        Assert.assertEquals("Unable to open source.",
                ex2.getMessage());

        // unknown error
        FixedTemplateGifSource source3 = new FixedTemplateGifSource(3);
        HandlingException ex3 = Assert.assertThrows(HandlingException.class,
                source3::loadIfNot);
        Assert.assertEquals("Unknown error.",
                ex3.getMessage());
    }

}