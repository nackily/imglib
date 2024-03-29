package cn.core.in;

import cn.core.utils.ObjectUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Buffered image data source whose from BufferedImage object.
 *
 * @author tracy
 * @since 0.2.1
 */
public class ThisBufferedImageSource implements BufferedImageSource<BufferedImage> {

    private final BufferedImage image;
    protected boolean readCompleted = false;

    public ThisBufferedImageSource(BufferedImage source) {
        if (ObjectUtils.isNull(source)) {
            throw new NullPointerException("BufferedImage is null.");
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
