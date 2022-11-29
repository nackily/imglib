package cn.core.is;

import cn.core.BufferedImageSource;
import cn.core.exec.HandlingException;

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
 * FileImageSource
 *
 * @author tracy
 * @since 1.0.0
 */
public class FileImageSource implements BufferedImageSource<File> {

    protected final File source;
    protected boolean readCompleted = false;
    protected String originalFormatName;

    public FileImageSource(File file) {
        if (file == null) {
            throw new NullPointerException("File is null");
        }
        this.source = file;
    }

    public FileImageSource(String filename) {
        if (filename == null || "".equals(filename)) {
            throw new NullPointerException("file name is null");
        }
        this.source = new File(filename);
    }

    @Override
    public BufferedImage read() throws IOException {
        if (readCompleted) {
            throw new HandlingException("file read has already completed");
        }
        // check the file is readable
        if (!source.canRead()) {
            throw new IIOException("cannot read the source file");
        }
        // get image input stream
        ImageInputStream stream = ImageIO.createImageInputStream(source);

        // read from file
        BufferedImage bi = read(stream);
        if (bi == null) {
            stream.close();
        }
        // mark the status to completed
        completeRead();
        return bi;
    }

    public BufferedImage read(ImageInputStream stream) throws IOException {
        Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);
        if (!iter.hasNext()) {
            return null;
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
