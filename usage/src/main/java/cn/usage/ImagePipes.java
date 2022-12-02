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
 * ImagePipes
 *
 * @author tracy
 * @since 1.0.0
 */
public final class ImagePipes {
    private ImagePipes() {}

    public static EmptySourceBuilder ofEmptySource() {
        return new EmptySourceBuilder();
    }

    public static ImageSourceBuilder<File> of(String... filenames) {
        ObjectUtils.excNull(filenames, "file names is null");
        CollectionUtils.excEmpty(filenames, "no any file name was specified");
        Iterator<String> iter = Arrays.asList(filenames).iterator();
        List<File> files = new ArrayList<>();
        while (iter.hasNext()) {
            files.add(new File(iter.next()));
        }
        return new ImageSourceBuilder<>(new FileImageSourceIterator(files));
    }

    public static ImageSourceBuilder<File> of(File... files) {
        ObjectUtils.excNull(files, "File array is null");
        CollectionUtils.excEmpty(files, "no any files was specified");
        return new ImageSourceBuilder<>(new FileImageSourceIterator(Arrays.asList(files)));
    }

    public static ImageSourceBuilder<InputStream> of(InputStream... iss) {
        ObjectUtils.excNull(iss, "InputStream array is null");
        CollectionUtils.excEmpty(iss, "no any input stream was specified");
        return new ImageSourceBuilder<>(new InputStreamImageSourceIterator(Arrays.asList(iss)));
    }

    public static ImageSourceBuilder<BufferedImage> of(BufferedImage... bis) {
        ObjectUtils.excNull(bis, "BufferedImage array is null");
        CollectionUtils.excEmpty(bis, "no any buffered image was specified");
        return new ImageSourceBuilder<>(new ThisImageSourceIterator(Arrays.asList(bis)));
    }

    public static ImageSourceBuilder<BufferedImage> of(Thumbnails.Builder<?> th) throws IOException {
        ObjectUtils.excNull(th, "Thumbnails.Builder is null");
        List<BufferedImage> images = th.asBufferedImages();
        return new ImageSourceBuilder<>(new ThisImageSourceIterator(images));
    }

    public static ImageSourceBuilder<BufferedImage> of(AbstractSourceBuilder<?> asb) throws IOException {
        ObjectUtils.excNull(asb, "Source builder is null");
        List<BufferedImage> images = asb.obtainBufferedImages();
        return new ImageSourceBuilder<>(new ThisImageSourceIterator(images));
    }

    public static PdfSourceBuilder<InputStream> ofPdf(InputStream is) {
        ObjectUtils.excNull(is, "PDF InputStream is null");
        return new PdfSourceBuilder<>(new InputStreamPdfSource(is));
    }

    public static PdfSourceBuilder<File> ofPdf(File pdf) {
        ObjectUtils.excNull(pdf, "PDF File is null");
        return new PdfSourceBuilder<>(new FilePdfSource(pdf));
    }

    public static PdfSourceBuilder<File> ofPdf(String filename) {
        if (StringUtils.isEmpty(filename)) {
            throw new InvalidSettingException("PDF file name is null");
        }
        return new PdfSourceBuilder<>(new FilePdfSource(new File(filename)));
    }

    public static PdfSourceBuilder<byte[]> ofPdf(byte[] bytes) {
        ObjectUtils.excNull(bytes, "PDF bytes is null");
        return new PdfSourceBuilder<>(new ByteArrayPdfSource(bytes));
    }

    public static GifSourceBuilder<InputStream> ofGif(InputStream is) {
        ObjectUtils.excNull(is, "GIF InputStream is null");
        return new GifSourceBuilder<>(new InputStreamGifSource(is));
    }

    public static GifSourceBuilder<File> ofGif(File gif) {
        ObjectUtils.excNull(gif, "GIF File is null");
        return new GifSourceBuilder<>(new FileGifSource(gif));
    }

    public static GifSourceBuilder<File> ofGif(String filename) {
        if (StringUtils.isEmpty(filename)) {
            throw new InvalidSettingException("GIF file name is null");
        }
        return new GifSourceBuilder<>(new FileGifSource(new File(filename)));
    }

    public static GifSourceBuilder<byte[]> ofGif(byte[] bytes) {
        ObjectUtils.excNull(bytes, "GIF bytes is null");
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
