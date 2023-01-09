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
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GifOutputStreamEncoderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void test_encode_correctUsage() throws IOException {
        // given
        File outputFile = temporaryFolder.newFile("output.gif");
        FileOutputStream fos = new FileOutputStream(outputFile);
        AbstractGifEncoder encoder = new GifOutputStreamEncoder.Builder()
                .delay(300)
                .repeat(1)
                .outputStream(fos)
                .build();
        List<BufferedImage> images = Arrays.asList(
                TestUtils.getImageFromResource("input/frame/0.jpg"),
                TestUtils.getImageFromResource("input/frame/1.jpg"),
                TestUtils.getImageFromResource("input/frame/2.jpg")
        );

        // when
        encoder.encode(images);

        // then
        GifDecoder decoder = new GifDecoder();
        decoder.read(outputFile.getAbsolutePath());
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

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            AnimatedGifEncoder encoder = new AnimatedGifEncoder();
            int delay = 300;
            int repeat = 2;
            OutputStream bos = new ByteArrayOutputStream();

            // when
            AbstractGifEncoder gifEncoder = new GifOutputStreamEncoder.Builder()
                    .encoder(encoder)
                    .delay(delay)
                    .repeat(repeat)
                    .reverse()
                    .outputStream(bos)
                    .build();

            // then
            Assert.assertEquals(encoder, ReflectionUtils.getFromSuper("encoder", gifEncoder));
            Assert.assertEquals(delay, ReflectionUtils.getFromSuper("delay", gifEncoder));
            Assert.assertEquals(repeat, ReflectionUtils.getFromSuper("repeat", gifEncoder));
            Assert.assertTrue((boolean) ReflectionUtils.getFromSuper("reverse", gifEncoder));
            Assert.assertEquals(bos, ReflectionUtils.get("stream", gifEncoder));
        }

        @Test
        public void testEx_build_notSetOutputStream() {
            GifOutputStreamEncoder.Builder builder = new GifOutputStreamEncoder.Builder();
            NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                    builder::build);
            Assert.assertEquals("OutputStream has not been set.", ex.getMessage());
        }

        @Test
        public void testEx_outputStream_null() {
            GifOutputStreamEncoder.Builder builder = new GifOutputStreamEncoder.Builder();
            NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                    () -> builder.outputStream(null));
            Assert.assertEquals("OutputStream is null.",
                    ex.getMessage());
        }
    }
}