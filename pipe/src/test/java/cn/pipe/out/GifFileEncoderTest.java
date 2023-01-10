package cn.pipe.out;

import cn.core.ex.InvalidSettingException;
import cn.pipe.ReflectionUtils;
import cn.pipe.TestUtils;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GifFileEncoderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void test_encode_correctUsage() throws IOException {
        // given
        String tempFilePath = temporaryFolder.newFile("output.gif").getAbsolutePath();
        AbstractGifEncoder encoder = new GifFileEncoder.Builder()
                .delay(300)
                .repeat(1)
                .filename(tempFilePath)
                .build();
        List<BufferedImage> images = Arrays.asList(
                TestUtils.getImageFromResource("input/frame/frame-0.jpg"),
                TestUtils.getImageFromResource("input/frame/frame-1.jpg"),
                TestUtils.getImageFromResource("input/frame/frame-2.jpg")
        );

        // when
        encoder.encode(images);

        // then
        GifDecoder decoder = new GifDecoder();
        decoder.read(tempFilePath);
        List<BufferedImage> restoredImages = new ArrayList<>();
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            restoredImages.add(decoder.getFrame(i));
        }

        /*
         * The gif format is a compressed format.The parsed buffered image cannot be
         * directly compared with the original image because original image has been
         * compressed when written to file.
         */
        Assert.assertEquals(images.size(), restoredImages.size());

        /*
         * The repeat count equals loop count, and the delay time of every frame is 300ms.
         */
        Assert.assertEquals(1, decoder.getLoopCount());
        Assert.assertEquals(300, decoder.getDelay(0));
        Assert.assertEquals(300, decoder.getDelay(1));
        Assert.assertEquals(300, decoder.getDelay(2));
    }

    @Test
    public void test_supportMultiple_alwaysTrue() {
        AbstractGifEncoder encoder = new GifFileEncoder.Builder()
                .filename("unset")
                .build();
        Assert.assertTrue(encoder.supportMultiple());
    }

    @Test
    public void test_encode_imagesReversed() throws IOException {
        // given
        String tempFilePath = temporaryFolder.newFile("output.gif").getAbsolutePath();
        AbstractGifEncoder encoder = new GifFileEncoder.Builder()
                .delay(300)
                .repeat(1)
                .reverse()
                .filename(tempFilePath)
                .build();
        List<BufferedImage> images = Arrays.asList(
                TestUtils.getImageFromResource("input/frame/frame-0.jpg"),
                TestUtils.getImageFromResource("input/frame/frame-1.jpg"),
                TestUtils.getImageFromResource("input/frame/frame-2.jpg")
        );

        // back up original image list
        List<BufferedImage> originalSort = new ArrayList<>(images);

        // when
        encoder.encode(images);

        // then
        int frameSize = originalSort.size();
        Assert.assertEquals(frameSize, images.size());

        // The order of the original list has been reversed.
        for (int i = 0; i < frameSize; i++) {
            Assert.assertEquals(originalSort.get(i), images.get(frameSize - i - 1));
        }
    }

    @Test
    public void testEx_encode_nullImageList() throws IOException {
        String tempFilePath = temporaryFolder.newFile("output.gif").getAbsolutePath();
        AbstractGifEncoder encoder = new GifFileEncoder.Builder()
                .delay(300)
                .repeat(1)
                .filename(tempFilePath)
                .build();
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> encoder.encode(null));
        Assert.assertEquals("Images to be encoded is null.", ex.getMessage());
    }

    @Test
    public void testEx_encode_emptyImageList() throws IOException {
        String tempFilePath = temporaryFolder.newFile("output.gif").getAbsolutePath();
        AbstractGifEncoder encoder = new GifFileEncoder.Builder()
                .delay(300)
                .repeat(1)
                .filename(tempFilePath)
                .build();
        List<BufferedImage> empty = Collections.emptyList();

        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> encoder.encode(empty));
        Assert.assertEquals("Images to be encoded is empty.", ex.getMessage());
    }

    public static class AbstractBuilderTest {

        @Test
        public void testEx_encoder_null() {
            GifFileEncoder.Builder builder = new GifFileEncoder.Builder();
            NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                    () -> builder.encoder(null));
            Assert.assertEquals("AnimatedGifEncoder is null.",
                    ex.getMessage());
        }

        @Test
        public void testEx_delay_invalid() {
            GifFileEncoder.Builder builder = new GifFileEncoder.Builder();
            InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                    () -> builder.delay(-1));
            Assert.assertEquals("Delay time cannot be less than 0.",
                    ex.getMessage());
        }

        @Test
        public void testEx_repeat_invalid() {
            GifFileEncoder.Builder builder = new GifFileEncoder.Builder();
            InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                    () -> builder.repeat(-1));
            Assert.assertEquals("The number of times the set of GIF frames cannot be less than 0.",
                    ex.getMessage());
        }

    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            AnimatedGifEncoder encoder = new AnimatedGifEncoder();
            int delay = 300;
            int repeat = 2;
            String filename = "/1/2/3/gif.gif";

            // when
            AbstractGifEncoder gifEncoder = new GifFileEncoder.Builder()
                    .encoder(encoder)
                    .delay(delay)
                    .repeat(repeat)
                    .reverse()
                    .filename(filename)
                    .build();

            // then
            Assert.assertEquals(encoder, ReflectionUtils.getFromSuper("encoder", gifEncoder));
            Assert.assertEquals(delay, ReflectionUtils.getFromSuper("delay", gifEncoder));
            Assert.assertEquals(repeat, ReflectionUtils.getFromSuper("repeat", gifEncoder));
            Assert.assertTrue((boolean) ReflectionUtils.getFromSuper("reverse", gifEncoder));
            Assert.assertEquals(filename, ReflectionUtils.get("filename", gifEncoder));
        }

        @Test
        public void test_build_correctUsage_withFile() {
            // given
            AnimatedGifEncoder encoder = new AnimatedGifEncoder();
            int delay = 300;
            int repeat = 2;
            File sourceFile = new File("/1/2/3/gif.gif");

            // when
            AbstractGifEncoder gifEncoder = new GifFileEncoder.Builder()
                    .encoder(encoder)
                    .delay(delay)
                    .repeat(repeat)
                    .reverse()
                    .file(sourceFile)
                    .build();

            // then
            Assert.assertEquals(encoder, ReflectionUtils.getFromSuper("encoder", gifEncoder));
            Assert.assertEquals(delay, ReflectionUtils.getFromSuper("delay", gifEncoder));
            Assert.assertEquals(repeat, ReflectionUtils.getFromSuper("repeat", gifEncoder));
            Assert.assertTrue((boolean) ReflectionUtils.getFromSuper("reverse", gifEncoder));
            File targetFile = new File((String) ReflectionUtils.get("filename", gifEncoder));
            Assert.assertEquals(sourceFile.getAbsolutePath(), targetFile.getAbsolutePath());
        }

        @Test
        public void test_build_defaultParam() {
            // given
            String filename = "/1/2/3/gif.gif";

            // when
            AbstractGifEncoder gifEncoder = new GifFileEncoder.Builder()
                    .filename(filename)
                    .build();

            // then
            Assert.assertNotNull(ReflectionUtils.getFromSuper("encoder", gifEncoder));
            Assert.assertEquals(0, ReflectionUtils.getFromSuper("delay", gifEncoder));
            Assert.assertEquals(0, ReflectionUtils.getFromSuper("repeat", gifEncoder));
            Assert.assertFalse((boolean) ReflectionUtils.getFromSuper("reverse", gifEncoder));
            Assert.assertEquals(filename, ReflectionUtils.get("filename", gifEncoder));
        }

        @Test
        public void testEx_build_notSetFilename() {
            GifFileEncoder.Builder builder = new GifFileEncoder.Builder();
            InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                    builder::build);
            Assert.assertEquals("Filename has not been set.", ex.getMessage());
        }

        @Test
        public void testEx_file_null() {
            GifFileEncoder.Builder builder = new GifFileEncoder.Builder();
            NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                    () -> builder.file(null));
            Assert.assertEquals("File is null.", ex.getMessage());
        }
    }
}