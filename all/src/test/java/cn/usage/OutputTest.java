package cn.usage;

import cn.core.BufferedImageEncoder;
import cn.core.ex.HandlingException;
import cn.core.ex.UnsupportedFormatException;
import cn.pipe.out.GifFileEncoder;
import com.madgag.gif.fmsware.GifDecoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class OutputTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private AbstractSourceBuilder<AbstractSourceBuilderTest.BatchSourceBuilder> builder;
    @Before
    public void init() {
        builder = new AbstractSourceBuilderTest.BatchSourceBuilder(3);
    }

    @Test
    public void test_toFile_withEncoder() throws IOException {
        // given
        File gifFile = temporaryFolder.newFile("tmp.gif");
        BufferedImageEncoder encoder = new GifFileEncoder.Builder()
                .file(gifFile)
                .delay(200)
                .repeat(1)
                .build();

        // when
        builder.toFile(encoder);

        // then
        GifDecoder decoder = new GifDecoder();
        decoder.read(gifFile.getAbsolutePath());
        Assert.assertEquals(3, decoder.getFrameCount());
    }

    @Test
    public void test_toFile_withFile() throws IOException {
        // given
        File f = temporaryFolder.newFile("tmp.png");
        AbstractSourceBuilder<AbstractSourceBuilderTest.BatchSourceBuilder> singleBuilder =
                new AbstractSourceBuilderTest.BatchSourceBuilder();

        // when
        singleBuilder.toFile(f);

        // then
        BufferedImage reloaded = ImageIO.read(f);

        Assert.assertEquals(30, reloaded.getWidth());
        Assert.assertEquals(45, reloaded.getHeight());

        Assert.assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
    }

    @Test
    public void test_toFile_withFilename() throws IOException {
        // given
        String filename = temporaryFolder.newFile("tmp.png").getAbsolutePath();
        AbstractSourceBuilder<AbstractSourceBuilderTest.BatchSourceBuilder> singleBuilder =
                new AbstractSourceBuilderTest.BatchSourceBuilder();

        // when
        singleBuilder.toFile(filename);

        // then
        BufferedImage reloaded = ImageIO.read(new File(filename));

        Assert.assertEquals(30, reloaded.getWidth());
        Assert.assertEquals(45, reloaded.getHeight());

        Assert.assertEquals("png", TestUtils.getFormatName(new FileInputStream(filename)));
    }

    @Test
    public void test_toFile_overwrittenFormat() throws IOException {
        // given
        String defineName = temporaryFolder.newFile("tmp.png").getAbsolutePath();
        AbstractSourceBuilder<AbstractSourceBuilderTest.BatchSourceBuilder> singleBuilder =
                new AbstractSourceBuilderTest.BatchSourceBuilder();

        // when
        singleBuilder.formatName("jpg").toFile(defineName);

        // then
        String rename = defineName + ".jpg";
        Assert.assertTrue(new File(rename).exists());

        BufferedImage reloaded = ImageIO.read(new File(rename));

        Assert.assertEquals(30, reloaded.getWidth());
        Assert.assertEquals(45, reloaded.getHeight());

        Assert.assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(rename)));
    }

    @Test
    public void testEx_toFile_notSetFormat() throws IOException {
        String filename = temporaryFolder.newFile("tmp").getAbsolutePath();
        AbstractSourceBuilder<AbstractSourceBuilderTest.BatchSourceBuilder> singleBuilder =
                new AbstractSourceBuilderTest.BatchSourceBuilder();
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                () -> singleBuilder.toFile(filename));
        Assert.assertEquals("No output format was specified.",
                ex.getMessage());
    }

    @Test
    public void test_toFiles_withFiles() throws IOException {
        // given
        File file1 = temporaryFolder.newFile("tmp-1.jpg");
        File file2 = temporaryFolder.newFile("tmp-2.jpg");
        File file3 = temporaryFolder.newFile("tmp-3.jpg");

        // when
        builder.toFiles(Arrays.asList(file1, file2, file3));

        // then
        BufferedImage reloaded1 = ImageIO.read(file1);
        Assert.assertEquals(30, reloaded1.getWidth());
        Assert.assertEquals(45, reloaded1.getHeight());
        Assert.assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(file1)));

        BufferedImage reloaded2 = ImageIO.read(file2);
        Assert.assertEquals(30, reloaded2.getWidth());
        Assert.assertEquals(45, reloaded2.getHeight());
        Assert.assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(file2)));

        BufferedImage reloaded3 = ImageIO.read(file3);
        Assert.assertEquals(30, reloaded3.getWidth());
        Assert.assertEquals(45, reloaded3.getHeight());
        Assert.assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(file3)));
    }

    @Test
    public void testEx_toFiles_withFiles_notEnough() throws IOException {
        List<File> files = Arrays.asList(temporaryFolder.newFile("tmp-1.jpg"),
                temporaryFolder.newFile("tmp-2.jpg"));
        IndexOutOfBoundsException ex = Assert.assertThrows(IndexOutOfBoundsException.class,
                () -> builder.toFiles(files));
        Assert.assertEquals("Not enough File provided by iterable.",
                ex.getMessage());
    }

    @Test
    public void test_toFiles_withFilenames() throws IOException {
        // given
        String filename1 = temporaryFolder.newFile("tmp-1.bmp").getAbsolutePath();
        String filename2 = temporaryFolder.newFile("tmp-2.bmp").getAbsolutePath();
        String filename3 = temporaryFolder.newFile("tmp-3.bmp").getAbsolutePath();

        // when
        builder.toFiles(filename1, filename2, filename3);

        // then
        BufferedImage reloaded1 = ImageIO.read(new File(filename1));
        Assert.assertEquals(30, reloaded1.getWidth());
        Assert.assertEquals(45, reloaded1.getHeight());
        Assert.assertEquals("bmp", TestUtils.getFormatName(new FileInputStream(filename1)));

        BufferedImage reloaded2 = ImageIO.read(new File(filename2));
        Assert.assertEquals(30, reloaded2.getWidth());
        Assert.assertEquals(45, reloaded2.getHeight());
        Assert.assertEquals("bmp", TestUtils.getFormatName(new FileInputStream(filename2)));

        BufferedImage reloaded3 = ImageIO.read(new File(filename3));
        Assert.assertEquals(30, reloaded3.getWidth());
        Assert.assertEquals(45, reloaded3.getHeight());
        Assert.assertEquals("bmp", TestUtils.getFormatName(new FileInputStream(filename3)));
    }

    @Test
    public void testEx_toFiles_withFilenames_null() throws IOException {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> builder.toFiles());
        Assert.assertEquals("File names is null.",
                ex.getMessage());
    }

    @Test
    public void testEx_toFiles_withFilenames_notEnough() throws IOException {
        String[] filenames = {temporaryFolder.newFile("tmp-1.jpg").getAbsolutePath(),
                temporaryFolder.newFile("tmp-2.jpg").getAbsolutePath()
        };
        IndexOutOfBoundsException ex = Assert.assertThrows(IndexOutOfBoundsException.class,
                () -> builder.toFiles(filenames));
        Assert.assertEquals("Not enough file name provided by iterator.",
                ex.getMessage());
    }

    @Test
    public void test_toOutputStream() throws IOException {
        // given
        AbstractSourceBuilder<AbstractSourceBuilderTest.BatchSourceBuilder> singleBuilder =
                new AbstractSourceBuilderTest.BatchSourceBuilder();
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        // when
        singleBuilder.formatName("png")
                .toOutputStream(os);

        // then
        BufferedImage reloaded = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));

        Assert.assertEquals(30, reloaded.getWidth());
        Assert.assertEquals(45, reloaded.getHeight());

        Assert.assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os.toByteArray())));
    }

    @Test
    public void test_toOutputStreams() throws IOException {
        // given
        ByteArrayOutputStream os1 = new ByteArrayOutputStream();
        ByteArrayOutputStream os2 = new ByteArrayOutputStream();
        ByteArrayOutputStream os3 = new ByteArrayOutputStream();

        // when
        builder.formatName("jpg")
                .toOutputStreams(Arrays.asList(os1, os2, os3));

        // then
        BufferedImage reloaded1 = ImageIO.read(new ByteArrayInputStream(os1.toByteArray()));
        Assert.assertEquals(30, reloaded1.getWidth());
        Assert.assertEquals(45, reloaded1.getHeight());
        Assert.assertEquals("JPEG", TestUtils.getFormatName(new ByteArrayInputStream(os1.toByteArray())));

        BufferedImage reloaded2 = ImageIO.read(new ByteArrayInputStream(os2.toByteArray()));
        Assert.assertEquals(30, reloaded2.getWidth());
        Assert.assertEquals(45, reloaded2.getHeight());
        Assert.assertEquals("JPEG", TestUtils.getFormatName(new ByteArrayInputStream(os2.toByteArray())));

        BufferedImage reloaded3 = ImageIO.read(new ByteArrayInputStream(os3.toByteArray()));
        Assert.assertEquals(30, reloaded3.getWidth());
        Assert.assertEquals(45, reloaded3.getHeight());
        Assert.assertEquals("JPEG", TestUtils.getFormatName(new ByteArrayInputStream(os3.toByteArray())));
    }

    @Test
    public void testEx_toOutputStreams_notEnough() {
        builder.formatName("png");
        List<OutputStream> outputStreams = Arrays.asList(new ByteArrayOutputStream(),
                new ByteArrayOutputStream());
        IndexOutOfBoundsException ex = Assert.assertThrows(IndexOutOfBoundsException.class,
                () -> builder.toOutputStreams(outputStreams));
        Assert.assertEquals("Not enough OutputStream provided by iterable.",
                ex.getMessage());
    }

    @Test
    public void testEx_toOutputStream_notSetFormatName() {
        AbstractSourceBuilder<AbstractSourceBuilderTest.BatchSourceBuilder> singleBuilder =
                new AbstractSourceBuilderTest.BatchSourceBuilder();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                () -> singleBuilder.toOutputStream(os));
        Assert.assertEquals("The output format is not set.",
                ex.getMessage());
    }

    @Test
    public void testEx_toOutputStream_unsupportedFormat() {
        AbstractSourceBuilder<AbstractSourceBuilderTest.BatchSourceBuilder> singleBuilder =
                new AbstractSourceBuilderTest.BatchSourceBuilder()
                        .formatName("any-format");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        UnsupportedFormatException ex = Assert.assertThrows(UnsupportedFormatException.class,
                () -> singleBuilder.toOutputStream(os));
        Assert.assertEquals("No appropriate writer is found for: any-format.",
                ex.getMessage());
    }

}
