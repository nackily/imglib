package cn.pipe.in.gif;

import cn.pipe.in.AbstractGifSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * ByteArrayGifSource
 *
 * @author tracy
 * @since 0.2.1
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
