package cn.usage;

import cn.core.exec.InvalidSettingException;
import cn.usage.builder.EmptySourceBuilder;
import cn.usage.builder.PdfSourceBuilder;
import net.coobird.thumbnailator.Thumbnails;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * 捕获器
 *
 * @author tracy
 * @since 1.0.0
 */
public final class Captors {
    private Captors() {}

    public static EmptySourceBuilder ofEmpty() {
        return new EmptySourceBuilder();
    }
    public static PdfSourceBuilder ofPdf(File file) throws FileNotFoundException {
        return new PdfSourceBuilder(file);
    }
    public static PdfSourceBuilder ofPdf(String filename) throws FileNotFoundException {
        return new PdfSourceBuilder(filename);
    }
    public static PdfSourceBuilder ofPdf(InputStream is) {
        return new PdfSourceBuilder(is);
    }


    @SuppressWarnings("unchecked")
    public abstract static class Builder<T, Children> {
        private final Children typeThis = (Children) this;
        protected final T source;
        protected String formatName = "PNG";

        protected Builder(T t) {
            this.source = t;
        }

        public Children formatName(String formatName) {
            if (formatName == null || "".equals(formatName)) {
                throw new InvalidSettingException("format name can not be null");
            }
            this.formatName = formatName;
            return typeThis;
        }

        public void toFile(File dest) throws IOException {
            BufferedImage image = obtainBufferedImage();
            ImageIO.write(image, formatName, dest);
        }

        public void toFile(String fn) throws IOException {
            toFile(new File(fn));
        }

        public void toOutputStream(OutputStream os) throws IOException {
            BufferedImage image = obtainBufferedImage();
            ImageIO.write(image, formatName, os);
        }

        public void toOutputStream(Iterator<OutputStream> oss) throws IOException {
            if (oss == null) {
                throw new NullPointerException("OutputStream iterator is null");
            }

            List<BufferedImage> images = obtainBufferedImages();
            for (BufferedImage o : images) {
                if (!oss.hasNext()) {
                    throw new IndexOutOfBoundsException("not enough OutputStream provided by iterator");
                }
                ImageIO.write(o, formatName, oss.next());
            }
        }

        public Thumbnails.Builder<BufferedImage> newThumbnails() {
            BufferedImage[] images = obtainBufferedImages().toArray(new BufferedImage[0]);
            return Thumbnails.of(images);
        }

        /**
         * 获得一张图像
         * @return 图像
         */
        public abstract BufferedImage obtainBufferedImage();

        /**
         * 获取图像
         * @return 图像集合
         */
        public abstract List<BufferedImage> obtainBufferedImages();

    }
}
