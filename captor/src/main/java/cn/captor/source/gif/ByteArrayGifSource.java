package cn.captor.source.gif;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * ByteArrayGifSource
 *
 * @author tracy
 * @since 1.0.0
 */
public class ByteArrayGifSource extends AbstractGifSource<byte[]> {

    public ByteArrayGifSource(byte[] source) {
        super(source);
    }

    @Override
    protected void loadIfNot() throws IOException {
        if (readCompleted) {
            return;
        }
        decoder.read(new ByteArrayInputStream(source));
        readCompleted = true;
    }
}
