package cn.usage;

import cn.core.BufferedImageEncoder;
import cn.core.PipeFilter;
import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.CollectionUtils;
import cn.core.utils.ObjectUtils;
import cn.core.utils.StringUtils;
import net.coobird.thumbnailator.Thumbnails;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * An abstract superclass of source builder.
 *
 * @param <T> The type of children.
 * @author tracy
 * @since 0.2.1
 */
@SuppressWarnings("unchecked")
public abstract class AbstractSourceBuilder<T> {

    public static final String NULL_FILTER = "PipeFilter is null.";
    private final T typeThis = (T) this;
    protected List<PipeFilter> filters = new ArrayList<>();

    /**
     * The output format name.
     */
    protected String formatName;

    /**
     * Set the output format name.
     * @param formatName The format name.
     * @return The object of children.
     */
    public T formatName(String formatName) {
        if (StringUtils.isEmpty(formatName)) {
            throw new InvalidSettingException("Format name can not be null.");
        }
        this.formatName = formatName;
        return typeThis;
    }

    /**
     * Add a filter to the end of the pipe.
     *
     * @param ypf The filter that to add.
     * @return The object of children.
     */
    public T addFilter(PipeFilter ypf) {
        ObjectUtils.excNull(ypf, NULL_FILTER);
        filters.add(ypf);
        return typeThis;
    }

    /**
     * Add filters to the end of the pipe.
     *
     * @param ypf The filters that to add.
     * @return The object of children.
     */
    public T addFilter(PipeFilter... ypf) {
        ObjectUtils.excNull(ypf, NULL_FILTER);
        filters.addAll(Arrays.asList(ypf));
        return typeThis;
    }

    /**
     * Remove a filter from the pipe.
     *
     * @param ypf The filters that to remove.
     * @return The object of children.
     */
    public T removeFilter(PipeFilter ypf) {
        ObjectUtils.excNull(ypf, NULL_FILTER);
        filters.remove(ypf);
        return typeThis;
    }

    /**
     * Obtain the buffered image.
     *
     * @return The buffered image.
     * @throws IOException If some I/O exceptions occurred.
     * @throws HandlingException If there is no image or multiple images in the pipe.
     */
    public BufferedImage obtainBufferedImage() throws IOException {
        List<BufferedImage> images = obtainBufferedImages();
        if (CollectionUtils.isNullOrEmpty(images)) {
            throw new HandlingException("No images was found.");
        }
        if (images.size() > 1) {
            throw new HandlingException("Cannot create an image from multiple original gif frames.");
        }
        return images.get(0);
    }

    /**
     * Obtain all buffered images.
     *
     * @return The buffered images.
     * @throws IOException If some I/O exceptions occurred.
     * @throws HandlingException If there is no image in the pipe.
     */
    public List<BufferedImage> obtainBufferedImages() throws IOException {
        List<BufferedImage> sourceImages = obtainSourceImages();
        if (CollectionUtils.isNullOrEmpty(sourceImages)) {
            throw new HandlingException("No images was found.");
        }
        // execute all filters
        List<BufferedImage> targetImages = sourceImages;
        for (PipeFilter ypf : filters) {
            targetImages = ypf.execute(sourceImages);
            sourceImages = targetImages;
        }
        return targetImages;
    }

    /**
     * Get all images from image sources.
     *
     * @return The images after loaded from sources.
     * @throws IOException If some I/O exceptions occurred when loading sources.
     */
    protected abstract List<BufferedImage> obtainSourceImages() throws IOException;


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

    public void toFile(File dest) throws IOException {
        BufferedImage image = obtainBufferedImage();
        File f = choseFormat(dest);
        BufferedImageUtils.write(image, formatName, f);
    }

    public void toFile(String fn) throws IOException {
        toFile(new File(fn));
    }

    public void toFiles(Iterator<File> fIter) throws IOException {
        ObjectUtils.excNull(fIter, "File iterator is null.");

        List<BufferedImage> images = obtainBufferedImages();
        for (BufferedImage o : images) {
            if (!fIter.hasNext()) {
                throw new IndexOutOfBoundsException("Not enough File provided by iterator.");
            }
            File f = choseFormat(fIter.next());
            BufferedImageUtils.write(o, formatName, f);
        }
    }

    public void toFiles(String... filenames) throws IOException {
        ObjectUtils.excNull(filenames, "File names is null.");

        List<BufferedImage> images = obtainBufferedImages();
        if (images.size() > filenames.length) {
            throw new IndexOutOfBoundsException("Not enough file name provided by iterator.");
        }
        for (int i = 0; i < filenames.length; i++) {
            File f = choseFormat(new File(filenames[i]));
            BufferedImageUtils.write(images.get(i), formatName, f);
        }
    }

    public void toOutputStream(OutputStream os) throws IOException {
        BufferedImage image = obtainBufferedImage();
        ImageIO.write(image, formatName, os);
    }

    public void toOutputStreams(Iterator<OutputStream> osIter) throws IOException {
        ObjectUtils.excNull(osIter, "OutputStream iterator is null.");

        List<BufferedImage> images = obtainBufferedImages();
        for (BufferedImage o : images) {
            if (!osIter.hasNext()) {
                throw new IndexOutOfBoundsException("Not enough OutputStream provided by iterator.");
            }
            ImageIO.write(o, formatName, osIter.next());
        }
    }

    private File choseFormat(File f) {
        ObjectUtils.excNull(f, "Output file is null.");
        String extension = StringUtils.getExtensionName(f);

        if (StringUtils.isEmpty(formatName)) {
            if (StringUtils.isEmpty(extension)) {
                throw new InvalidSettingException("No output format was specified.");
            }
            // initial the output format name
            formatName = extension;
        } else {
            // adjust the output file's if necessary
            if (!extension.equalsIgnoreCase(formatName)) {
                String newFilename = f.getAbsolutePath() + "." + formatName;
                return new File(newFilename);
            }
        }
        return f;
    }

}
