package cn.core.in;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * InputStreamImageSource
 *
 * @author tracy
 * @since 1.0.0
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
