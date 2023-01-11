package cn.core.in;

import cn.core.ex.UnsupportedFormatException;
import cn.core.utils.ObjectUtils;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Buffered image data source whose from input stream.
 *
 * @author tracy
 * @since 0.2.1
 */
public class InputStreamImageSource implements BufferedImageSource<InputStream> {

    protected final InputStream source;
    protected boolean readCompleted = false;
    protected String originalFormatName;

    public InputStreamImageSource(InputStream source) {
        if (ObjectUtils.isNull(source)) {
            throw new NullPointerException("InputStream is null.");
        }
        this.source = source;
    }

    @Override
    public BufferedImage read() throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        if (!readers.hasNext()) {
            // can not parse input stream
            iis.close();
            throw new UnsupportedFormatException("No suitable ImageReader found for source input stream.");
        }

        ImageReader reader = readers.next();
        ImageReadParam param = reader.getDefaultReadParam();
        reader.setInput(iis);

        BufferedImage image;
        try {
            // original format name
            originalFormatName = reader.getFormatName();
            // read image
            image = reader.read(0, param);
        } finally {
            reader.dispose();
            iis.close();
        }

        readCompleted = true;
        return image;
    }


    @Override
    public String getOriginalFormatName() {
        return originalFormatName;
    }

    @Override
    public InputStream getSource() {
        return source;
    }

    @Override
    public boolean isReadCompleted() {
        return readCompleted;
    }

}
