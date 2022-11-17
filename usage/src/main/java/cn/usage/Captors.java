package cn.usage;

import cn.captor.source.InputStreamPdfSource;
import cn.core.BufferedImageSource;
import cn.core.PipeBuilder;
import cn.core.utils.CollectionUtils;
import cn.usage.builder.EmptySourceBuilder;
import cn.usage.builder.PdfSourceBuilder;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

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
        CollectionUtils.excNull(is, "InputStream is null");
        return new PdfSourceBuilder<>(new InputStreamPdfSource(is));
    }


    @SuppressWarnings("unchecked")
    public abstract static class Builder<S, Children> extends PipeBuilder<S> {

        private final Children typeThis = (Children) this;

        public Children formatName(String formatName) {
            super.setFormatName(formatName);
            return typeThis;
        }

        public Thumbnails.Builder<BufferedImage> newThumbnails() throws IOException {
            BufferedImage[] images = obtainBufferedImages().toArray(new BufferedImage[0]);
            return Thumbnails.of(images);
        }
    }
}
