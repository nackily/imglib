package cn.core.in;

import cn.core.ex.HandlingException;
import cn.core.ex.UnsupportedFormatException;
import cn.core.utils.ObjectUtils;
import cn.core.utils.StringUtils;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Buffered image data source whose from image File.
 *
 * @author tracy
 * @since 0.2.1
 */
public class FileImageSource implements BufferedImageSource<File> {

    protected final File source;
    protected boolean readCompleted = false;
    protected String originalFormatName;

    public FileImageSource(File file) {
        if (ObjectUtils.isNull(file)) {
            throw new NullPointerException("File is null.");
        }
        this.source = file;
    }

    public FileImageSource(String filename) {
        if (StringUtils.isEmpty(filename)) {
            throw new NullPointerException("File name is null.");
        }
        this.source = new File(filename);
    }

    @Override
    public BufferedImage read() throws IOException {
        if (readCompleted) {
            throw new HandlingException("File reading has already completed.");
        }
        // check the file is readable
        if (!source.canRead()) {
            throw new IIOException("Cannot read the source file.");
        }
        // get image input stream
        ImageInputStream stream = ImageIO.createImageInputStream(source);

        // read from file
        BufferedImage bi = read(stream);

        // mark the status to completed
        completeRead();
        return bi;
    }

    protected BufferedImage read(ImageInputStream stream) throws IOException {
        Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);
        if (!iter.hasNext()) {
            // can not parse input stream
            stream.close();
            throw new UnsupportedFormatException("No suitable ImageReader found for source file.");
        }

        ImageReader reader = iter.next();
        ImageReadParam param = reader.getDefaultReadParam();
        reader.setInput(stream, true, true);

        // set the format
        originalFormatName = reader.getFormatName();

        BufferedImage bi;
        try {
            bi = reader.read(0, param);
        } finally {
            reader.dispose();
            stream.close();
        }
        return bi;
    }

    @Override
    public String getOriginalFormatName() {
        return originalFormatName;
    }

    @Override
    public File getSource() {
        return source;
    }

    @Override
    public boolean isReadCompleted() {
        return readCompleted;
    }

    protected void completeRead() {
        this.readCompleted = true;
    }

}
