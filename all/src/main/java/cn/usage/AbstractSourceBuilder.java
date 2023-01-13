package cn.usage;

import cn.core.BufferedImageEncoder;
import cn.core.PipeFilter;
import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.ex.UnsupportedFormatException;
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
        CollectionUtils.excEmpty(ypf, "No PipeFilter need to be added.");
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
            throw new HandlingException("Cannot create an image from multiple original image sources.");
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
        writeFile(image, dest);
    }

    public void toFile(String fn) throws IOException {
        toFile(new File(fn));
    }

    public void toFiles(Iterable<File> iterable) throws IOException {
        ObjectUtils.excNull(iterable, "File iterable is null.");

        List<BufferedImage> images = obtainBufferedImages();
        Iterator<File> iter = iterable.iterator();
        for (BufferedImage o : images) {
            if (!iter.hasNext()) {
                throw new IndexOutOfBoundsException("Not enough File provided by iterable.");
            }
            writeFile(o, iter.next());
        }
    }

    public void toFiles(String... filenames) throws IOException {
        if (CollectionUtils.isNullOrEmpty(filenames)) {
            throw new NullPointerException("File names is null.");
        }

        List<BufferedImage> images = obtainBufferedImages();
        if (images.size() > filenames.length) {
            throw new IndexOutOfBoundsException("Not enough file name provided by iterator.");
        }
        for (int i = 0; i < filenames.length; i++) {
            writeFile(images.get(i), new File(filenames[i]));
        }
    }

    public void toOutputStream(OutputStream os) throws IOException {
        writeStream(obtainBufferedImage(), os);
    }

    public void toOutputStreams(Iterable<OutputStream> iterable) throws IOException {
        ObjectUtils.excNull(iterable, "OutputStream iterable is null.");

        List<BufferedImage> images = obtainBufferedImages();
        Iterator<OutputStream> iter = iterable.iterator();
        for (BufferedImage o : images) {
            if (!iter.hasNext()) {
                throw new IndexOutOfBoundsException("Not enough OutputStream provided by iterable.");
            }
            writeStream(o, iter.next());
        }
    }

    protected void writeFile(BufferedImage image, File file) throws IOException {
        File f = choseFormat(file);
        BufferedImageUtils.write(image, formatName, f);
    }

    protected void writeStream(BufferedImage image, OutputStream os) throws IOException {
        if (StringUtils.isEmpty(formatName)) {
            throw new HandlingException("The output format is not set.");
        }
        if (!ImageIO.write(image, formatName, os)) {
            throw new UnsupportedFormatException(String
                    .format("No appropriate writer is found for: %s.", formatName));
        }
    }

    /**
     * Select a format for this input file.
     *
     * <li>Use the original input file when {@link AbstractSourceBuilder#formatName}
     * is null and input file contains a extension.</li>
     *
     * <li>Append {@link AbstractSourceBuilder#formatName} to path of input file when
     * {@link AbstractSourceBuilder#formatName} is not null.</li>
     *
     * <DD>
     *     May get an unexpected file path when {@link AbstractSourceBuilder#formatName}
     *     is not null and input file contains a extension.
     *     <pre>
     *         ...
     *         .formatName("png")
     *         .toFile("/test.jpg");
     *     </pre>
     *     Then, we will get a file name of <code>test.jpg.png</code>.
     *     <br>
     *     Otherwise, a similar situation will occur when exporting to various files.
     *     <pre>
     *         ...
     *         .toFiles("test1.png", "test2.jpg", "test3.bmp");
     *     </pre>
     *     Then, we will get a list files name of <code>test1.png</code>,
     *     <code>test2.jpg.png</code>, <code>test3.bmp.png</code>.
     * </DD>
     *
     * @param f The file object of input.
     * @return The final file object, maybe after adjust.
     * @throws InvalidSettingException When {@link AbstractSourceBuilder#formatName}
     * is null and input file not contains an extension.
     */
    private File choseFormat(File f) {
        ObjectUtils.excNull(f, "Output file is null.");
        String extension = StringUtils.getExtensionName(f);

        if (StringUtils.isEmpty(formatName)) {
            if (StringUtils.isEmpty(extension)) {
                throw new HandlingException("No output format was specified.");
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
