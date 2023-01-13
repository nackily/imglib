package cn.usage;

import cn.core.ex.InvalidSettingException;
import cn.core.in.BufferedImageSource;
import cn.core.in.FileImageSource;
import cn.core.in.InputStreamImageSource;
import cn.core.in.ThisBufferedImageSource;
import cn.core.utils.CollectionUtils;
import cn.core.utils.ObjectUtils;
import cn.core.utils.StringUtils;
import cn.pipe.in.gif.ByteArrayGifSource;
import cn.pipe.in.gif.FileGifSource;
import cn.pipe.in.gif.InputStreamGifSource;
import cn.pipe.in.pdf.ByteArrayPdfSource;
import cn.pipe.in.pdf.FilePdfSource;
import cn.pipe.in.pdf.InputStreamPdfSource;
import cn.usage.builder.EmptySourceBuilder;
import cn.usage.builder.GifSourceBuilder;
import cn.usage.builder.ImageSourceBuilder;
import cn.usage.builder.PdfSourceBuilder;
import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Provides a fluent interface to processing image.
 * <p>
 *     This is the main entry point for processing image with <B>Imglib</B>.
 * </p>
 * <DL>
 * <DT><B>Usage:</B></DT>
 * <DD>
 * The following example code demonstrates how to use the fluent interface
 * to create a hash avatar for the user, the avatar setting to 8px*8px,
 * the image size setting to 300px*300px, and add a border with a margin of 20px.
 * <pre>
ImagePipes.ofEmptySource()
    .register(new HashImageGenerator.Builder("Tracy")
        .gridVerticalNum(8)
        .bgColor(ColorUtils.of(240, 240, 240))
        .fgColor(ColorUtils.of(50, 150, 50))
        .build())
    .toThumbnails()
    .addFilter(new HighQualityExpandHandler.Builder()
        .finalWidth(300)
        .keepAspectRatio(true)
        .build())
    .addFilter(new BorderHandler.Builder()
        .fillColor(ColorUtils.of(200, 200, 200))
        .vMargins(20)
        .hMargins(20)
        .build())
    .scale(1.0)
    .toFile(".../avatar.png");
 * </pre>
 * </DD>
 * </DL>
 *
 * For more examples, please visit the <a href="https://github.com/nackily/imglib">
 * Imglib</a> project page.
 *
 * @author tracy
 * @since 0.2.1
 */
public final class ImagePipes {
    private ImagePipes() {}

    public static EmptySourceBuilder ofEmptySource() {
        return new EmptySourceBuilder();
    }

    public static ImageSourceBuilder<File> of(String... filenames) {
        ObjectUtils.excNull(filenames, "File names is null.");
        CollectionUtils.excEmpty(filenames, "Not any file name was specified.");
        Iterator<String> iter = Arrays.asList(filenames).iterator();
        List<File> files = new ArrayList<>();
        while (iter.hasNext()) {
            files.add(new File(iter.next()));
        }
        return new ImageSourceBuilder<>(new FileImageSourceIterator(files));
    }

    public static ImageSourceBuilder<File> of(File... files) {
        ObjectUtils.excNull(files, "File array is null.");
        CollectionUtils.excEmpty(files, "Not any files was specified.");
        return new ImageSourceBuilder<>(new FileImageSourceIterator(Arrays.asList(files)));
    }

    public static ImageSourceBuilder<InputStream> of(InputStream... iss) {
        ObjectUtils.excNull(iss, "InputStream array is null.");
        CollectionUtils.excEmpty(iss, "No any input stream was specified");
        return new ImageSourceBuilder<>(new InputStreamImageSourceIterator(Arrays.asList(iss)));
    }

    public static ImageSourceBuilder<BufferedImage> of(BufferedImage... bis) {
        ObjectUtils.excNull(bis, "BufferedImage array is null.");
        CollectionUtils.excEmpty(bis, "Not any buffered image was specified.");
        return new ImageSourceBuilder<>(new ThisImageSourceIterator(Arrays.asList(bis)));
    }

    public static ImageSourceBuilder<BufferedImage> of(Thumbnails.Builder<?> th) throws IOException {
        ObjectUtils.excNull(th, "Thumbnails.Builder is null.");
        List<BufferedImage> images = th.asBufferedImages();
        return new ImageSourceBuilder<>(new ThisImageSourceIterator(images));
    }

    public static ImageSourceBuilder<BufferedImage> of(AbstractSourceBuilder<?> asb) throws IOException {
        ObjectUtils.excNull(asb, "Source builder is null.");
        List<BufferedImage> images = asb.obtainBufferedImages();
        return new ImageSourceBuilder<>(new ThisImageSourceIterator(images));
    }

    public static PdfSourceBuilder<InputStream> ofPdf(InputStream is) {
        ObjectUtils.excNull(is, "Pdf InputStream is null.");
        return new PdfSourceBuilder<>(new InputStreamPdfSource(is));
    }

    public static PdfSourceBuilder<File> ofPdf(File pdf) {
        ObjectUtils.excNull(pdf, "Pdf File is null.");
        return new PdfSourceBuilder<>(new FilePdfSource(pdf));
    }

    public static PdfSourceBuilder<File> ofPdf(String filename) {
        if (StringUtils.isEmpty(filename)) {
            throw new InvalidSettingException("Pdf file name is null.");
        }
        return new PdfSourceBuilder<>(new FilePdfSource(new File(filename)));
    }

    public static PdfSourceBuilder<byte[]> ofPdf(byte[] bytes) {
        ObjectUtils.excNull(bytes, "Pdf byte array is null.");
        return new PdfSourceBuilder<>(new ByteArrayPdfSource(bytes));
    }

    public static GifSourceBuilder<InputStream> ofGif(InputStream is) {
        ObjectUtils.excNull(is, "Gif InputStream is null.");
        return new GifSourceBuilder<>(new InputStreamGifSource(is));
    }

    public static GifSourceBuilder<File> ofGif(File gif) {
        ObjectUtils.excNull(gif, "Gif File is null.");
        return new GifSourceBuilder<>(new FileGifSource(gif));
    }

    public static GifSourceBuilder<File> ofGif(String filename) {
        if (StringUtils.isEmpty(filename)) {
            throw new InvalidSettingException("Gif file name is null.");
        }
        return new GifSourceBuilder<>(new FileGifSource(new File(filename)));
    }

    public static GifSourceBuilder<byte[]> ofGif(byte[] bytes) {
        ObjectUtils.excNull(bytes, "Gif byte array is null.");
        return new GifSourceBuilder<>(new ByteArrayGifSource(bytes));
    }


    private static class FileImageSourceIterator implements Iterable<BufferedImageSource<File>> {

        private final Iterable<File> files;

        private FileImageSourceIterator(Iterable<File> files) {
            this.files = files;
        }

        @Override
        public Iterator<BufferedImageSource<File>> iterator() {
            return new Iterator<BufferedImageSource<File>>() {
                private final Iterator<File> iter = files.iterator();
                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }
                @Override
                public BufferedImageSource<File> next() {
                    return new FileImageSource(iter.next());
                }
                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    private static class InputStreamImageSourceIterator implements Iterable<BufferedImageSource<InputStream>> {

        private final Iterable<InputStream> iss;

        private InputStreamImageSourceIterator(Iterable<InputStream> iss) {
            this.iss = iss;
        }

        @Override
        public Iterator<BufferedImageSource<InputStream>> iterator() {
            return new Iterator<BufferedImageSource<InputStream>>() {
                private final Iterator<InputStream> iter = iss.iterator();
                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }
                @Override
                public BufferedImageSource<InputStream> next() {
                    return new InputStreamImageSource(iter.next());
                }
                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    private static class ThisImageSourceIterator implements Iterable<BufferedImageSource<BufferedImage>> {

        private final Iterable<BufferedImage> bis;

        private ThisImageSourceIterator(Iterable<BufferedImage> bis) {
            this.bis = bis;
        }

        @Override
        public Iterator<BufferedImageSource<BufferedImage>> iterator() {
            return new Iterator<BufferedImageSource<BufferedImage>>() {
                private final Iterator<BufferedImage> iter = bis.iterator();
                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }
                @Override
                public BufferedImageSource<BufferedImage> next() {
                    return new ThisBufferedImageSource(iter.next());
                }
                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }
}
