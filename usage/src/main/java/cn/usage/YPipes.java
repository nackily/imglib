package cn.usage;

import cn.core.BufferedImageEncoder;
import cn.core.BufferedImageSource;
import cn.core.AbstractPipeBuilder;
import cn.core.YPipeFilter;
import cn.core.exec.HandlingException;
import cn.core.is.FileImageSource;
import cn.core.is.InputStreamImageSource;
import cn.core.is.ThisBufferedImageSource;
import cn.core.utils.CollectionUtils;
import cn.core.utils.ObjectUtils;
import cn.core.utils.StringUtils;
import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

/**
 * YPipes
 *
 * @author tracy
 * @since 1.0.0
 */
public final class YPipes {

    public static final String NULL_FILTER = "Y-PipeFilter is null";

    private YPipes(){}

    public static Builder<File> of(String... filenames) {
        ObjectUtils.excNull(filenames, "file names is null");
        CollectionUtils.excEmpty(filenames, "no any file name was specified");
        Iterator<String> iter = Arrays.asList(filenames).iterator();
        List<File> files = new ArrayList<>();
        while (iter.hasNext()) {
            files.add(new File(iter.next()));
        }
        return new Builder<>(new Builder.FileImageSourceIterator(files));
    }

    public static Builder<File> of(File... files) {
        ObjectUtils.excNull(files, "File array is null");
        CollectionUtils.excEmpty(files, "no any files was specified");
        return new Builder<>(new Builder.FileImageSourceIterator(Arrays.asList(files)));
    }

    public static Builder<InputStream> of(InputStream... iss) {
        ObjectUtils.excNull(iss, "InputStream array is null");
        CollectionUtils.excEmpty(iss, "no any input stream was specified");
        return new Builder<>(new Builder.InputStreamImageSourceIterator(Arrays.asList(iss)));
    }

    public static Builder<BufferedImage> of(BufferedImage... bis) {
        ObjectUtils.excNull(bis, "BufferedImage array is null");
        CollectionUtils.excEmpty(bis, "no any buffered image was specified");
        return new Builder<>(new Builder.ThisImageSourceIterator(Arrays.asList(bis)));
    }
    public static Builder<BufferedImage> of(Thumbnails.Builder<?> th) throws IOException {
        ObjectUtils.excNull(th, "Thumbnails.Builder is null");
        List<BufferedImage> images = th.asBufferedImages();
        return new Builder<>(new Builder.ThisImageSourceIterator(images));
    }

    public static Builder<BufferedImage> of(Captors.AbstractBuilder<?> ca) throws IOException {
        ObjectUtils.excNull(ca, "Captors.Builder is null");
        List<BufferedImage> images = ca.obtainBufferedImages();
        return new Builder<>(new Builder.ThisImageSourceIterator(images));
    }


    public static class Builder<P> extends AbstractPipeBuilder<Builder<P>> {

        protected boolean useOriginalFormat;
        protected final Iterable<BufferedImageSource<P>> sources;
        protected List<YPipeFilter> filters = new ArrayList<>();

        protected Builder(Iterable<BufferedImageSource<P>> sources) {
            this.sources = sources;
        }

        public Builder<P> addLast(YPipeFilter ypf) {
            ObjectUtils.excNull(ypf, NULL_FILTER);
            filters.add(ypf);
            return this;
        }

        public Builder<P> addLast(YPipeFilter... ypf) {
            ObjectUtils.excNull(ypf, NULL_FILTER);
            filters.addAll(Arrays.asList(ypf));
            return this;
        }

        public Builder<P> remove(YPipeFilter ypf) {
            ObjectUtils.excNull(ypf, NULL_FILTER);
            filters.remove(ypf);
            return this;
        }

        @Override
        public BufferedImage obtainBufferedImage() throws IOException {
            List<BufferedImage> tars = obtainBufferedImages();
            return tars.get(0);
        }

        @Override
        public List<BufferedImage> obtainBufferedImages() throws IOException {
            // obtain the source images
            List<BufferedImage> originalImages = new ArrayList<>();
            List<String> formatNames = new ArrayList<>();
            for (BufferedImageSource<P> o : sources) {
                originalImages.add(o.read());
                formatNames.add(o.getOriginalFormatName());
            }
            // setting the format name
            if (useOriginalFormat) {
                String[] formats = formatNames.stream().filter(StringUtils::isNotEmpty).distinct().toArray(String[]::new);
                if (formats.length == 0) {
                    throw new HandlingException("no available original format");
                } else if (formats.length > 1) {
                    throw new HandlingException(MessageFormat.format("multiple available original formats found:[{0}]",
                            StringUtils.join(formats)));
                } else {
                    super.formatName(formats[0]);
                }
            }

            // execute all filters
            List<BufferedImage> targetImages = originalImages;
            for (YPipeFilter ypf : filters) {
                targetImages = ypf.execute(originalImages);
                originalImages = targetImages;
            }
            return targetImages;
        }

        public Builder<P> useOriginalFormat() {
            useOriginalFormat = true;
            return this;
        }


        public Thumbnails.Builder<BufferedImage> toThumbnails() throws IOException {
            BufferedImage[] images = obtainBufferedImages().toArray(new BufferedImage[0]);
            return Thumbnails.of(images);
        }

        public void toFile(BufferedImageEncoder encoder) throws IOException {
            if (encoder.supportMultiple()) {
                encoder.encode(obtainBufferedImages());
            } else {
                encoder.encode(Collections.singletonList(obtainBufferedImage()));
            }
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
}
