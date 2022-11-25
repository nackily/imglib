package cn.usage;

import cn.captor.source.impl.ByteArrayPdfSource;
import cn.captor.source.impl.FilePdfSource;
import cn.captor.source.impl.InputStreamPdfSource;
import cn.core.PipeBuilder;
import cn.usage.builder.EmptySourceBuilder;
import cn.usage.builder.PdfSourceBuilder;
import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 捕获器
 *
 * @author tracy
 * @since 1.0.0
 */
public final class Captors {
    private Captors() {}

    public static EmptySourceBuilder ofEmptySource() {
        return new EmptySourceBuilder();
    }

    public static PdfSourceBuilder<InputStream> ofPdf(InputStream is) {
        if (is == null) {
            throw new NullPointerException("InputStream is null");
        }
        return new PdfSourceBuilder<>(new InputStreamPdfSource(is));
    }

    public static PdfSourceBuilder<File> ofPdf(File pdf) {
        if (pdf == null) {
            throw new NullPointerException("File is null");
        }
        return new PdfSourceBuilder<>(new FilePdfSource(pdf));
    }

    public static PdfSourceBuilder<File> ofPdf(String filename) {
        if (filename == null) {
            throw new NullPointerException("file name is null");
        }
        return new PdfSourceBuilder<>(new FilePdfSource(new File(filename)));
    }

    public static PdfSourceBuilder<byte[]> ofPdf(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        return new PdfSourceBuilder<>(new ByteArrayPdfSource(bytes));
    }


    @SuppressWarnings("unchecked")
    public abstract static class Builder<S, Children> extends PipeBuilder<S> {

        private final Children typeThis = (Children) this;

        public Children formatName(String formatName) {
            super.setFormatName(formatName);
            return typeThis;
        }

        public Thumbnails.Builder<BufferedImage> toThumbnails() throws IOException {
            BufferedImage[] images = obtainBufferedImages().toArray(new BufferedImage[0]);
            return Thumbnails.of(images);
        }

        public YPipes.Builder<BufferedImage> toYPipes() throws IOException {
            BufferedImage[] images = obtainBufferedImages().toArray(new BufferedImage[0]);
            return YPipes.of(images);
        }
    }
}
