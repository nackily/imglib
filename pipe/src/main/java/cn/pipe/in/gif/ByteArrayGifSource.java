package cn.pipe.in.gif;

import cn.pipe.in.AbstractGifSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * A gif source that from byte array.
 *
 * @author tracy
 * @since 0.2.1
 */
public class ByteArrayGifSource extends AbstractGifSource<byte[]> {

    public ByteArrayGifSource(byte[] source) {
        super(source);
    }

    @Override
    protected int doLoad() throws IOException {
        int status = decoder.read(new ByteArrayInputStream(source));
        readCompleted = true;
        return status;
    }
}
