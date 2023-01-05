package cn.core.utils;

import cn.core.TestUtils;
import cn.core.ex.InvalidSettingException;
import cn.core.ex.UnsupportedFormatException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BufferedImageUtilsTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void test_newBackgroundImage_correctUsage_transparent() {
        // given
        int width = 200;
        int height = 100;
        float alpha = 0f;

        // when
        BufferedImage image = BufferedImageUtils.newBackgroundImage(alpha, width, height, null);

        // then
        Assert.assertEquals(width, image.getWidth());
        Assert.assertEquals(height, image.getHeight());

        int rgb = image.getRGB(0, 0);
        // pixel transparent when equal to 0
        boolean isTransparent = (rgb >> 24) == 0;
        Assert.assertTrue(isTransparent);
    }

    @Test
    public void test_newBackgroundImage_correctUsage_colored() {
        // given
        int width = 200, height = 100;
        int r = 15, g = 89, b = 231;
        Color color = ColorUtils.of(r, g, b);

        // when
        BufferedImage image = BufferedImageUtils.newBackgroundImage(0.5f, width, height, color);

        // then
        Assert.assertEquals(width, image.getWidth());
        Assert.assertEquals(height, image.getHeight());

        int rgb = image.getRGB(0, 0);

        // alpha
        if (image.getType() != BufferedImage.TYPE_INT_ARGB) {
            Assert.fail("Unexpected image type.");
        }
        int alphaComponent = (rgb >> 24) & 0xff;
        // alpha=0.5 means translucency, the standard alpha value -> 255 * 0.5 = 127.5 -> 128
        Assert.assertEquals(128, alphaComponent);

        // RGB
        int redComponent = (rgb >> 16) & 0xFF;
        int greenComponent = (rgb >> 8) & 0xFF;
        int blueComponent = rgb & 0xFF;
        Assert.assertEquals(r, redComponent);
        Assert.assertEquals(g, greenComponent);
        Assert.assertEquals(b, blueComponent);
    }

    @Test
    public void testEx_newBackgroundImage_invalidWidth() {
        Color color = ColorUtils.random();
        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> BufferedImageUtils.newBackgroundImage(0, 100, color));
        Assert.assertEquals("The image width must be greater than 0.",
                exception.getMessage());
    }

    @Test
    public void testEx_newBackgroundImage_invalidHeight() {
        Color color = ColorUtils.random();
        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> BufferedImageUtils.newBackgroundImage(100, 0, color));
        Assert.assertEquals("The image height must be greater than 0.",
                exception.getMessage());
    }

    @Test
    public void testEx_newBackgroundImage_invalidAlpha() {
        Color color = ColorUtils.random();
        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> BufferedImageUtils.newBackgroundImage(1.1f, 100, 100, color));
        Assert.assertEquals("Alpha out of bounds:[0, 1].",
                exception.getMessage());
    }


    @Test
    public void test_write_correctUsage_withFilename() throws IOException {
        // given
        BufferedImage image = TestUtils.getImageFromResource("input/nothing.jpg");
        String filename = temporaryFolder.newFile("output.png").getAbsolutePath();

        // when
        BufferedImageUtils.write(image, filename);

        // then
        boolean exists = new File(filename).exists();
        Assert.assertTrue(exists);
    }

    @Test
    public void test_write_correctUsage_withFilename_withFormat() throws IOException {
        // given
        BufferedImage image = TestUtils.getImageFromResource("input/nothing.jpg");
        String filename = temporaryFolder.newFile("output.png").getAbsolutePath();
        String formatName = "PNG";

        // when
        BufferedImageUtils.write(image, filename, formatName);

        // then
        File targetFile = new File(filename);
        Assert.assertTrue(targetFile.exists());
    }

    @Test
    public void test_write_correctUsage_withFile() throws IOException {
        // given
        BufferedImage image = TestUtils.getImageFromResource("input/nothing.jpg");
        File targetFile = temporaryFolder.newFile("output.png");

        // when
        BufferedImageUtils.write(image, targetFile);

        // then
        Assert.assertTrue(targetFile.exists());
    }

    @Test
    public void test_write_correctUsage_withFile_withFormat() throws IOException {
        // given
        BufferedImage image = TestUtils.getImageFromResource("input/nothing.jpg");
        File targetFile = temporaryFolder.newFile("output.png");
        String formatName = "PNG";

        // when
        BufferedImageUtils.write(image, formatName, targetFile);

        // then
        Assert.assertTrue(targetFile.exists());
    }

    @Test
    public void testEx_write_withNullParameter() throws IOException {
        BufferedImage image = TestUtils.getImageFromResource("input/nothing.jpg");
        File target = temporaryFolder.newFile("output");

        // null BufferedImage
        NullPointerException nullImageException = Assert.assertThrows(NullPointerException.class,
                () -> BufferedImageUtils.write(null, "PNG", target));
        Assert.assertEquals("Buffered image is null.",
                nullImageException.getMessage());

        // null FormatName
        NullPointerException nullFormatException = Assert.assertThrows(NullPointerException.class,
                () -> BufferedImageUtils.write(image, null, target));
        Assert.assertEquals("Output format name is null.",
                nullFormatException.getMessage());

        // null File
        NullPointerException nullFileException = Assert.assertThrows(NullPointerException.class,
                () -> BufferedImageUtils.write(image, "PNG", (File) null));
        Assert.assertEquals("Output file is null.",
                nullFileException.getMessage());
    }

    @Test
    public void testEx_write_wrongFormat() throws IOException {
        BufferedImage image = TestUtils.getImageFromResource("input/nothing.jpg");
        File target = temporaryFolder.newFile("output");
        String format = "TIFF";

        // assert exception
        UnsupportedFormatException exception = Assert.assertThrows(UnsupportedFormatException.class,
                () -> BufferedImageUtils.write(image, format, target));
        Assert.assertEquals("No suitable ImageWriter found for TIFF.",
                exception.getMessage());
    }

}