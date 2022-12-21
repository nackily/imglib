package cn.core.in;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Buffered image data source whose from input stream.
 *
 * @author tracy
 * @since 0.2.1
 */
public class InputStreamImageSource implements BufferedImageSource<InputStream> {

    protected final InputStream source;
    protected boolean readCompleted = false;

    public InputStreamImageSource(InputStream source) {
        if (source == null) {
            throw new NullPointerException("InputStream is null.");
        }
        this.source = source;
    }

    @Override
    public BufferedImage read() throws IOException {
        BufferedImage image = ImageIO.read(source);
        readCompleted = true;
        return image;
    }


    @Override
    public String getOriginalFormatName() {
        return null;
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
