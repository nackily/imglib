package cn.core.in;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ThisBufferedImageSource
 *
 * @author tracy
 * @since 1.0.0
 */
public class ThisBufferedImageSource implements BufferedImageSource<BufferedImage> {

    private final BufferedImage image;
    protected boolean readCompleted = false;

    public ThisBufferedImageSource(BufferedImage source) {
        if (source == null) {
            throw new NullPointerException("BufferedImage is null");
        }
        this.image = source;
    }

    @Override
    public BufferedImage read() throws IOException {
        readCompleted = true;
        return image;
    }

    @Override
    public String getOriginalFormatName() {
        return null;
    }

    @Override
    public BufferedImage getSource() {
        return image;
    }

    @Override
    public boolean isReadCompleted() {
        return readCompleted;
    }
}
