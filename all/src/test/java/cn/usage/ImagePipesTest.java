package cn.usage;

import cn.core.ex.InvalidSettingException;
import cn.core.in.BufferedImageSource;
import cn.core.in.GifSource;
import cn.core.in.PdfSource;
import cn.usage.builder.EmptySourceBuilder;
import cn.usage.builder.GifSourceBuilder;
import cn.usage.builder.ImageSourceBuilder;
import cn.usage.builder.PdfSourceBuilder;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.Assert;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class ImagePipesTest {

    @Test
    public void test_ofEmptySource() {
        // when
        EmptySourceBuilder builder = ImagePipes.ofEmptySource();

        // then
        Assert.assertNotNull(builder);
    }

    @Test
    public void test_of_fromFilenames() {
        // given
        String[] filenames = {"file1", "file2", "file3"};

        // when
        ImageSourceBuilder<File> builder = ImagePipes.of(filenames);

        // then
        Iterable<BufferedImageSource<File>> sources = (Iterable<BufferedImageSource<File>>)
                ReflectionUtils.get("sources", builder);
        Iterator<BufferedImageSource<File>> iterator = sources.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            BufferedImageSource<File> next = iterator.next();
            Assert.assertEquals(filenames[index], next.getSource().getName());
            index++;
        }
    }

    @Test
    public void testEx_of_fromFilenames_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.of((String[]) null));
        Assert.assertEquals("File names is null.",
                ex.getMessage());
    }

    @Test
    public void testEx_of_fromFilenames_empty() {
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> ImagePipes.of(new String[0]));
        Assert.assertEquals("Not any file name was specified.",
                ex.getMessage());
    }

    @Test
    public void test_of_fromFiles() {
        // given
        File[] files = {new File("file1"), new File("file2"), new File("file3")};

        // when
        ImageSourceBuilder<File> builder = ImagePipes.of(files);

        // then
        Iterable<BufferedImageSource<File>> sources = (Iterable<BufferedImageSource<File>>)
                ReflectionUtils.get("sources", builder);
        Iterator<BufferedImageSource<File>> iterator = sources.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            BufferedImageSource<File> next = iterator.next();
            Assert.assertEquals(files[index], next.getSource());
            index++;
        }
    }

    @Test
    public void testEx_of_fromFiles_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.of((File[]) null));
        Assert.assertEquals("File array is null.",
                ex.getMessage());
    }

    @Test
    public void testEx_of_fromFiles_empty() {
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> ImagePipes.of(new File[0]));
        Assert.assertEquals("Not any files was specified.",
                ex.getMessage());
    }

    @Test
    public void test_of_fromInputStreams() {
        // given
        InputStream[] iss = {
                new ByteArrayInputStream("anything1".getBytes()),
                new ByteArrayInputStream("anything2".getBytes()),
                new ByteArrayInputStream("anything3".getBytes()),
        };

        // when
        ImageSourceBuilder<InputStream> builder = ImagePipes.of(iss);

        // then
        Iterable<BufferedImageSource<InputStream>> sources = (Iterable<BufferedImageSource<InputStream>>)
                ReflectionUtils.get("sources", builder);
        Iterator<BufferedImageSource<InputStream>> iterator = sources.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            BufferedImageSource<InputStream> next = iterator.next();
            Assert.assertEquals(iss[index], next.getSource());
            index++;
        }
    }

    @Test
    public void testEx_of_fromInputStreams_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.of((InputStream[]) null));
        Assert.assertEquals("InputStream array is null.",
                ex.getMessage());
    }

    @Test
    public void testEx_of_fromInputStreams_empty() {
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> ImagePipes.of(new InputStream[0]));
        Assert.assertEquals("No any input stream was specified",
                ex.getMessage());
    }

    @Test
    public void test_of_fromBufferedImages() {
        // given
        BufferedImage[] bis = {
                new BufferedImage(30, 40, BufferedImage.TYPE_INT_ARGB),
                new BufferedImage(50, 70, BufferedImage.TYPE_INT_RGB),
                new BufferedImage(10, 80, BufferedImage.TYPE_INT_ARGB)
        };

        // when
        ImageSourceBuilder<BufferedImage> builder = ImagePipes.of(bis);

        // then
        Iterable<BufferedImageSource<BufferedImage>> sources = (Iterable<BufferedImageSource<BufferedImage>>)
                ReflectionUtils.get("sources", builder);
        Iterator<BufferedImageSource<BufferedImage>> iterator = sources.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            BufferedImageSource<BufferedImage> next = iterator.next();
            Assert.assertEquals(bis[index], next.getSource());
            index++;
        }
    }

    @Test
    public void testEx_of_fromBufferedImages_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.of((BufferedImage[]) null));
        Assert.assertEquals("BufferedImage array is null.",
                ex.getMessage());
    }

    @Test
    public void testEx_of_fromBufferedImages_empty() {
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> ImagePipes.of(new BufferedImage[0]));
        Assert.assertEquals("Not any buffered image was specified.",
                ex.getMessage());
    }

    @Test
    public void test_of_fromThumbnails() throws IOException {
        // given
        Thumbnails.Builder<BufferedImage> thumbnailsBuilder = Thumbnails.of(
                new BufferedImage(30, 40, BufferedImage.TYPE_INT_ARGB),
                new BufferedImage(50, 70, BufferedImage.TYPE_INT_RGB),
                new BufferedImage(10, 80, BufferedImage.TYPE_INT_ARGB)
        ).scale(1);


        // when
        ImageSourceBuilder<BufferedImage> builder = ImagePipes.of(thumbnailsBuilder);

        // then
        Iterable<BufferedImageSource<BufferedImage>> sources = (Iterable<BufferedImageSource<BufferedImage>>)
                ReflectionUtils.get("sources", builder);
        Iterator<BufferedImageSource<BufferedImage>> iterator = sources.iterator();

        List<BufferedImage> thumbnails = thumbnailsBuilder.asBufferedImages();

        int index = 0;
        while (iterator.hasNext()) {
            BufferedImageSource<BufferedImage> next = iterator.next();
            // Compare images.
            Assert.assertTrue(BufferedImageComparer.isSame(thumbnails.get(index), next.getSource()));
            index++;
        }
    }

    @Test
    public void testEx_of_fromThumbnails_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.of((Thumbnails.Builder<?>) null));
        Assert.assertEquals("Thumbnails.Builder is null.",
                ex.getMessage());
    }

    @Test
    public void test_of_fromSourceBuilder() throws IOException {
        // given
        AbstractSourceBuilder<?> asb = ImagePipes.of(
                new BufferedImage(30, 40, BufferedImage.TYPE_INT_ARGB),
                new BufferedImage(50, 70, BufferedImage.TYPE_INT_RGB),
                new BufferedImage(10, 80, BufferedImage.TYPE_INT_ARGB)
        );


        // when
        ImageSourceBuilder<BufferedImage> builder = ImagePipes.of(asb);

        // then
        Iterable<BufferedImageSource<BufferedImage>> sources = (Iterable<BufferedImageSource<BufferedImage>>)
                ReflectionUtils.get("sources", builder);
        Iterator<BufferedImageSource<BufferedImage>> iterator = sources.iterator();

        List<BufferedImage> images = asb.obtainBufferedImages();

        int index = 0;
        while (iterator.hasNext()) {
            BufferedImageSource<BufferedImage> next = iterator.next();
            Assert.assertEquals(images.get(index), next.getSource());
            index++;
        }
    }

    @Test
    public void testEx_of_fromSourceBuilder_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.of((AbstractSourceBuilder<?>) null));
        Assert.assertEquals("Source builder is null.",
                ex.getMessage());
    }

    @Test
    public void test_ofPdf_fromInputStream() {
        // given
        InputStream is = new ByteArrayInputStream("any".getBytes());

        // when
        PdfSourceBuilder<InputStream> builder = ImagePipes.ofPdf(is);

        // then
        PdfSource<InputStream> source = (PdfSource<InputStream>)
                ReflectionUtils.get("source", builder);

        Assert.assertEquals(is, source.getSource());
    }

    @Test
    public void testEx_ofPdf_fromInputStream_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.ofPdf((InputStream) null));
        Assert.assertEquals("Pdf InputStream is null.",
                ex.getMessage());
    }

    @Test
    public void test_ofPdf_fromFile() {
        // given
        File f = new File("file1");

        // when
        PdfSourceBuilder<File> builder = ImagePipes.ofPdf(f);

        // then
        PdfSource<File> source = (PdfSource<File>)
                ReflectionUtils.get("source", builder);

        Assert.assertEquals(f, source.getSource());
    }

    @Test
    public void testEx_ofPdf_fromFile_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.ofPdf((File) null));
        Assert.assertEquals("Pdf File is null.",
                ex.getMessage());
    }

    @Test
    public void test_ofPdf_fromFilename() {
        // given
        String filename = "file1";

        // when
        PdfSourceBuilder<File> builder = ImagePipes.ofPdf(filename);

        // then
        PdfSource<File> source = (PdfSource<File>)
                ReflectionUtils.get("source", builder);

        Assert.assertEquals(filename, source.getSource().getName());
    }

    @Test
    public void testEx_ofPdf_fromFilename_null() {
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> ImagePipes.ofPdf((String) null));
        Assert.assertEquals("Pdf file name is null.",
                ex.getMessage());
    }

    @Test
    public void test_ofPdf_fromByteArray() {
        // given
        byte[] bytes = "file1".getBytes();

        // when
        PdfSourceBuilder<byte[]> builder = ImagePipes.ofPdf(bytes);

        // then
        PdfSource<byte[]> source = (PdfSource<byte[]>)
                ReflectionUtils.get("source", builder);

        Assert.assertArrayEquals(bytes, source.getSource());
    }

    @Test
    public void testEx_ofPdf_fromByteArray_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.ofPdf((byte[]) null));
        Assert.assertEquals("Pdf byte array is null.",
                ex.getMessage());
    }

    @Test
    public void test_ofGif_fromInputStream() {
        // given
        InputStream is = new ByteArrayInputStream("any".getBytes());

        // when
        GifSourceBuilder<InputStream> builder = ImagePipes.ofGif(is);

        // then
        GifSource<InputStream> source = (GifSource<InputStream>)
                ReflectionUtils.get("source", builder);

        Assert.assertEquals(is, source.getSource());
    }

    @Test
    public void testEx_ofGif_fromInputStream_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.ofGif((InputStream) null));
        Assert.assertEquals("Gif InputStream is null.",
                ex.getMessage());
    }

    @Test
    public void test_ofGif_fromFile() {
        // given
        File f = new File("file1");

        // when
        GifSourceBuilder<File> builder = ImagePipes.ofGif(f);

        // then
        GifSource<File> source = (GifSource<File>)
                ReflectionUtils.get("source", builder);

        Assert.assertEquals(f, source.getSource());
    }

    @Test
    public void testEx_ofGif_fromFile_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.ofGif((File) null));
        Assert.assertEquals("Gif File is null.",
                ex.getMessage());
    }

    @Test
    public void test_ofGif_fromFilename() {
        // given
        String filename = "file1";

        // when
        GifSourceBuilder<File> builder = ImagePipes.ofGif(filename);

        // then
        GifSource<File> source = (GifSource<File>)
                ReflectionUtils.get("source", builder);

        Assert.assertEquals(filename, source.getSource().getName());
    }

    @Test
    public void testEx_ofGif_fromFilename_null() {
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> ImagePipes.ofGif((String) null));
        Assert.assertEquals("Gif file name is null.",
                ex.getMessage());
    }

    @Test
    public void test_ofGif_fromByteArray() {
        // given
        byte[] bytes = "file1".getBytes();

        // when
        GifSourceBuilder<byte[]> builder = ImagePipes.ofGif(bytes);

        // then
        GifSource<byte[]> source = (GifSource<byte[]>)
                ReflectionUtils.get("source", builder);

        Assert.assertArrayEquals(bytes, source.getSource());
    }

    @Test
    public void testEx_ofGif_fromByteArray_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> ImagePipes.ofGif((byte[]) null));
        Assert.assertEquals("Gif byte array is null.",
                ex.getMessage());
    }

}